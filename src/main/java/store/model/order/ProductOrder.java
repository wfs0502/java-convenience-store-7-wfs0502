package store.model.order;

import store.model.product.Product;

public class ProductOrder {
    private final Product product;
    private final int quantity;

    public ProductOrder(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
