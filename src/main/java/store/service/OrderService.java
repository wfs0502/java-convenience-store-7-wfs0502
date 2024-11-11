package store.service;

import java.util.HashMap;
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
        for (Map.Entry<Product, Integer> entry : productOrders.getItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            applyPromotion(product, quantity);
        }
    }

    private void applyPromotion(Product product, int requestedQuantity) {
        Promotion promotion = promotions.findPromotionByName(product.getPromotion());
        if (promotion == null || !promotion.isPromotionActive()) {
            product.decreaseGeneralStock(requestedQuantity);
            return;
        }

        int applicablePromotionQuantity = applyApplicablePromotion(product, requestedQuantity, promotion);
        int remainingQuantity = requestedQuantity - applicablePromotionQuantity;
        handleRemainingQuantity(product, remainingQuantity);
        offerExtraItemsIfAvailable(product, requestedQuantity, promotion);
    }

    private int applyApplicablePromotion(Product product, int requestedQuantity, Promotion promotion) {
        int promotionStock = product.getPromotionQuantity();
        int buyQuantity = promotion.getBuyQuantity();
        int freeQuantity = promotion.getFreeQuantity();

        int maxApplicablePromotionQuantity = calculateApplicablePromotionQuantity(promotionStock, requestedQuantity,
                buyQuantity, freeQuantity);
        product.decreasePromotionStock(maxApplicablePromotionQuantity);
        return maxApplicablePromotionQuantity;
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

    private void offerExtraItemsIfAvailable(Product product, int requestedQuantity, Promotion promotion) {
        int extraFreeQuantity = calculateExtraFreeQuantity(requestedQuantity, promotion.getBuyQuantity(),
                promotion.getFreeQuantity(), product.getPromotionQuantity());
        if (extraFreeQuantity > 0 && promptForExtraItems(product.getName(), extraFreeQuantity)) {
            product.decreasePromotionStock(extraFreeQuantity);
        }
    }

    private boolean promptForExtraItems(String productName, int extraQuantity) {
        outputView.printForExtraItems(productName, extraQuantity);
        return inputView.readYesOrNo().equals("Y");
    }

    private int calculateApplicablePromotionQuantity(int promotionStock, int requestedQuantity, int buyQuantity,
                                                     int freeQuantity) {
        int promotionUnit = buyQuantity + freeQuantity;
        int maxApplicablePromotionQuantity = (promotionStock / promotionUnit) * promotionUnit;
        return Math.min(maxApplicablePromotionQuantity, requestedQuantity);
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
}
