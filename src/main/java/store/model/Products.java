package store.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Products {
    private final LinkedHashMap<String, Product> products;

    public Products() {
        this.products = new LinkedHashMap<>();
    }

    public void addProduct(Product product) {
        this.products.put(product.getName(), product);
    }

    public Product findProductByName(String name) {
        return products.get(name);
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products.values());
    }
}
