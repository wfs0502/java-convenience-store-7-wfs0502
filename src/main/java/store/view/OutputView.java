package store.view;

import java.util.List;
import java.util.Objects;
import store.model.product.Product;
import store.model.product.Products;

public class OutputView {
    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String INVENTORY_HEADER = "현재 보유하고 있는 상품입니다.";
    private static final String PROMOTION_STOCK_MESSAGE = "- %s %,d원 %s %s%n";
    private static final String GENERAL_STOCK_MESSAGE = "- %s %,d원 %s%n";
    private static final String REGULAR_PRICE_MESSAGE = "\n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)%n";
    private static final String EXTRA_ITEMS_MESSAGE = "\n현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n";
    private static final String MEMBERSHIP_MESSAGE = "\n멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String NO_STOCK = "재고 없음";
    private static final String QUANTITY_UNIT = "개";

    public void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
        System.out.println(INVENTORY_HEADER);
        System.out.println();
    }

    public void printProducts(Products products) {
        for (Product product : products.getProducts()) {
            printProduct(product);
        }
        System.out.println();
    }

    private void printProduct(Product product) {
        String name = product.getName();
        int price = product.getPrice();
        String promotionQuantity = formatQuantity(product.getPromotionQuantity());
        String generalQuantity = formatQuantity(product.getGeneralQuantity());
        String promotion = formatPromotion(product.getPromotion());

        if (!Objects.equals(promotionQuantity, NO_STOCK)) {
            System.out.printf(PROMOTION_STOCK_MESSAGE, name, price, promotionQuantity, promotion);
        }
        System.out.printf(GENERAL_STOCK_MESSAGE, name, price, generalQuantity);
    }

    private String formatQuantity(int initialQuantity) {
        String quantity = initialQuantity + QUANTITY_UNIT;
        if (initialQuantity == 0) {
            quantity = NO_STOCK;
        }
        return quantity;
    }

    private String formatPromotion(String promotion) {
        if (promotion == null) {
            promotion = "";
        }
        return promotion;
    }

    public void printForMembership() {
        System.out.println(MEMBERSHIP_MESSAGE);
    }

    public void printForRegularPrice(String name, int quantity) {
        System.out.printf(REGULAR_PRICE_MESSAGE, name, quantity);
    }

    public void printForExtraItems(String name, int quantity) {
        System.out.printf(EXTRA_ITEMS_MESSAGE, name, quantity);
    }

    public void printErrorMessage(String message) {
        System.out.println(message);
    }

    public void printOrderSummary(List<String> productDetails, List<String> freeItems, int totalQuantity, int totalAmount,
                                  int promotionDiscount, int membershipDiscount, int finalAmount) {
        System.out.println();
        System.out.println("==============W 편의점================");
        System.out.println("상품명\t\t수량\t금액");

        for (String detail : productDetails) {
            System.out.println(detail);
        }

        if (!freeItems.isEmpty()) {
            System.out.println("=============증    정===============");
            for (String freeItem : freeItems) {
                System.out.println(freeItem);
            }
        }

        System.out.println("====================================");
        System.out.printf("총구매액\t\t%d\t%,d\n", totalQuantity, totalAmount);
        System.out.printf("행사할인\t\t\t-%,d\n", promotionDiscount);
        System.out.printf("멤버십할인\t\t\t-%,d\n", membershipDiscount);
        System.out.printf("내실돈\t\t\t %,d\n", finalAmount);
        System.out.println();
    }

    public void printContinueMessage() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
    }
}
