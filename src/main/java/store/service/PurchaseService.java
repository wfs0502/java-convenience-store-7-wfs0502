package store.service;

import java.util.Map;
import store.model.product.Product;
import store.model.product.Products;
import store.model.purchase.Purchase;

public class PurchaseService {
    private static final String NON_PRODUCT_ERROR = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String OVER_QUANTITY_ERROR = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";
    private final Products products;

    public PurchaseService(Products products) {
        this.products = products;
    }

    public void purchase(Purchase purchase) {
        for (Map.Entry<String, Integer> entry : purchase.getItems().entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            Product product = products.findProductByName(productName);

            validate(product, quantity);
        }
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
