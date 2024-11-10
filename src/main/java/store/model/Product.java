package store.model;

public class Product {
    private final String name;
    private final int price;
    private final String promotion;
    private int promotionQuantity;
    private int generalQuantity;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.promotionQuantity = 0;
        this.generalQuantity = 0;
        this.promotion = promotion;
        updateQuantity(quantity, promotion);
    }

    public void updateQuantity(int quantity, String promotion) {
        if (promotion == null) {
            generalQuantity += quantity;
            return;
        }
        promotionQuantity += quantity;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getPromotion() {
        return promotion;
    }

    public int getPromotionQuantity() {
        return promotionQuantity;
    }

    public int getGeneralQuantity() {
        return generalQuantity;
    }
}
