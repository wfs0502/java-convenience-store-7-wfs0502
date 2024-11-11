package store.model.order;

import java.util.HashMap;
import java.util.Map;
import store.model.product.Product;

public class ProductOrders {
    private final Map<Product, Integer> items;

    public ProductOrders(Map<Product, Integer> items) {
        this.items = items;
    }

    public Map<Product, Integer> getItems() {
        return new HashMap<>(items);
    }
}
