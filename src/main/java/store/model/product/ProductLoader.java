package store.model.product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProductLoader {
    private final static String FILE_PATH = "src/main/resources/products.md";

    public Products load() {
        Products products = new Products();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            reader.readLine(); // 파일의 첫번째 줄은 건너뜀
            reader.lines().forEach(line -> updateProducts(products, line));

            return products;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateProducts(Products products, String line) {
        Product newProduct = parseProduct(line);
        Product existingProduct = products.findProductByName(newProduct.getName());

        if (existingProduct != null) {
            existingProduct.updateQuantity(newProduct.getGeneralQuantity(), newProduct.getPromotion());
            return;
        }
        products.addProduct(newProduct);
    }

    private Product parseProduct(String line) {
        String[] fields = line.split(",");
        String name = fields[0];
        int price = Integer.parseInt(fields[1]);
        int quantity = Integer.parseInt(fields[2]);
        String promotion = handleNullString(fields[3]);

        return new Product(name, price, quantity, promotion);
    }

    private String handleNullString(String string) {
        if (string.equals("null")) {
            return null;
        }
        return string;
    }
}
