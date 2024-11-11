package store.view;

import java.util.Objects;
import store.model.Product;
import store.model.Products;

public class OutputView {
    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String INVENTORY_HEADER = "현재 보유하고 있는 상품입니다.";
    private static final String PROMOTION_STOCK_MESSAGE = "- %s %,d원 %s %s%n";
    private static final String GENERAL_STOCK_MESSAGE = "- %s %,d원 %s%n";
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

    public void printErrorMessage(String message) {
        System.out.println(message);
    }
}
