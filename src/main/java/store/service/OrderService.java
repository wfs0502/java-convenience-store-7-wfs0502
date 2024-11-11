package store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.model.product.Product;
import store.model.product.Products;
import store.model.order.ProductOrders;
import store.model.promotion.Promotion;
import store.model.promotion.Promotions;
import store.view.InputView;
import store.view.OutputView;

public class OrderService {
    private static final String NON_PRODUCT_ERROR = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String OVER_QUANTITY_ERROR = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;
    private final Products products;
    private final Promotions promotions;
    private final InputView inputView;
    private final OutputView outputView;

    public OrderService(Products products, Promotions promotions, InputView inputView, OutputView outputView) {
        this.products = products;
        this.promotions = promotions;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void order(ProductOrders productOrders) {
        int totalAmount = 0;
        int totalQuantity = 0;
        int promotionDiscount = 0;
        int membershipDiscount = 0;
        int nonPromotionAmount = 0;

        List<String> productDetails = new ArrayList<>();
        List<String> freeItems = new ArrayList<>();

        for (Map.Entry<Product, Integer> entry : productOrders.getItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            int productTotal = product.getPrice() * quantity;
            totalAmount += productTotal;
            totalQuantity += quantity;

            productDetails.add(String.format("%s\t\t%d\t%,d", product.getName(), quantity, productTotal));

            if (isPromotionAvailable(product)) {
                promotionDiscount += applyPromotionDiscount(product, quantity, freeItems);
            }
            if (!isPromotionAvailable(product)) {
                product.decreaseGeneralStock(quantity);
                nonPromotionAmount += productTotal;
            }
        }

        boolean applyMembership = promptForMembership();
        if (applyMembership) {
            membershipDiscount = calculateMembershipDiscount(nonPromotionAmount);
        }

        int finalAmount = totalAmount - promotionDiscount - membershipDiscount;
        outputOrderSummary(productDetails, freeItems, totalQuantity, totalAmount, promotionDiscount, membershipDiscount,
                finalAmount);
    }

    private boolean isPromotionAvailable(Product product) {
        Promotion promotion = promotions.findPromotionByName(product.getPromotion());
        return promotion != null && promotion.isPromotionActive();
    }

    private int calculateMembershipDiscount(int nonPromotionTotal) {
        int discount = (int) (nonPromotionTotal * MEMBERSHIP_DISCOUNT_RATE);
        return Math.min(discount, MAX_MEMBERSHIP_DISCOUNT);
    }

    private boolean promptForMembership() {
        outputView.printForMembership();
        return inputView.readYesOrNo().equals("Y");
    }

    private int applyPromotionDiscount(Product product, int requestedQuantity, List<String> freeItems) {
        Promotion promotion = promotions.findPromotionByName(product.getPromotion());

        int buyQuantity = promotion.getBuyQuantity();
        int promotionFreeQuantity = promotion.getFreeQuantity();
        int promotionUnit = buyQuantity + promotionFreeQuantity;

        int promotionUnits = requestedQuantity / promotionUnit;
        int totalFreeQuantity = promotionUnits * promotionFreeQuantity;

        int applicablePromotionQuantity = applyApplicablePromotion(product, requestedQuantity, promotion);
        int freeQuantity = applicablePromotionQuantity / (promotion.getBuyQuantity() + promotion.getFreeQuantity());
        int basicDiscount = freeQuantity * product.getPrice();
        int remainingQuantity = requestedQuantity - applicablePromotionQuantity;

        boolean extraItemPrompted = offerExtraItemsIfAvailable(product, requestedQuantity, promotion);
        if (totalFreeQuantity > 0) {
            freeItems.add(String.format("%s\t\t%d", product.getName(), totalFreeQuantity));
        }
        if (remainingQuantity > 0 && !extraItemPrompted) {
            handleRemainingQuantity(product, remainingQuantity);
        }

        return basicDiscount;
    }

    private int applyApplicablePromotion(Product product, int requestedQuantity, Promotion promotion) {
        int promotionStock = product.getPromotionQuantity();
        int buyQuantity = promotion.getBuyQuantity();
        int freeQuantity = promotion.getFreeQuantity();

        int applicablePromotionQuantity = calculateApplicablePromotionQuantity(promotionStock, requestedQuantity,
                buyQuantity, freeQuantity);
        product.decreasePromotionStock(applicablePromotionQuantity);
        return applicablePromotionQuantity;
    }

    private int calculateApplicablePromotionQuantity(int promotionStock, int requestedQuantity, int buyQuantity,
                                                     int freeQuantity) {
        int promotionUnit = buyQuantity + freeQuantity;
        int requestedPromotionQuantity = (requestedQuantity / promotionUnit) * promotionUnit;
        int availablePromotionQuantity = (promotionStock / promotionUnit) * promotionUnit;
        return Math.min(requestedPromotionQuantity, availablePromotionQuantity);
    }

    private void handleRemainingQuantity(Product product, int remainingQuantity) {
        if (remainingQuantity > 0 && promptForRegularPrice(product.getName(), remainingQuantity)) {
            product.decreaseGeneralStock(remainingQuantity);
        }
    }

    private boolean promptForRegularPrice(String productName, int remainingQuantity) {
        outputView.printForRegularPrice(productName, remainingQuantity);
        return inputView.readYesOrNo().equals("Y");
    }

    private boolean offerExtraItemsIfAvailable(Product product, int requestedQuantity, Promotion promotion) {
        int extraFreeQuantity = calculateExtraFreeQuantity(requestedQuantity, promotion.getBuyQuantity(),
                promotion.getFreeQuantity(), product.getPromotionQuantity());

        if (extraFreeQuantity > 0) {
            if (promptForExtraItems(product.getName(), extraFreeQuantity)) {
                product.decreasePromotionStock(extraFreeQuantity);
            }
            return true;
        }
        return false;
    }

    private int calculateExtraFreeQuantity(int requestedQuantity, int buyQuantity, int freeQuantity,
                                           int promotionStock) {
        int possibleSets = requestedQuantity / buyQuantity;
        int possibleFreeQuantity = possibleSets * freeQuantity;
        int alreadyReceivedFreeQuantity = requestedQuantity / (buyQuantity + freeQuantity) * freeQuantity;
        int extraFreeQuantity = possibleFreeQuantity - alreadyReceivedFreeQuantity;

        int totalNeededQuantity = requestedQuantity + extraFreeQuantity;
        if (totalNeededQuantity > promotionStock) {
            extraFreeQuantity = 0;
        }
        return extraFreeQuantity;
    }

    private boolean promptForExtraItems(String productName, int extraQuantity) {
        outputView.printForExtraItems(productName, extraQuantity);
        return inputView.readYesOrNo().equals("Y");
    }

    public ProductOrders createProductOrders(Map<String, Integer> orderItems) {
        Map<Product, Integer> productItems = new HashMap<>();
        for (Map.Entry<String, Integer> entry : orderItems.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            Product product = products.findProductByName(productName);

            validate(product, quantity);
            productItems.put(product, quantity);
        }
        return new ProductOrders(productItems);
    }

    private void validate(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException(NON_PRODUCT_ERROR);
        }
        if (!product.hasSufficientStock(quantity)) {
            throw new IllegalArgumentException(OVER_QUANTITY_ERROR);
        }
    }

    private void outputOrderSummary(List<String> productDetails, List<String> freeItems, int totalQuantity,
                                    int totalAmount, int promotionDiscount, int membershipDiscount, int finalAmount) {
        outputView.printOrderSummary(productDetails, freeItems, totalQuantity, totalAmount, promotionDiscount,
                membershipDiscount, finalAmount);
    }
}
