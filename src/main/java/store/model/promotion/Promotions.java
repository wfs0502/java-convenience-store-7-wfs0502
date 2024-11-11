package store.model.promotion;

import java.util.HashMap;
import java.util.Map;

public class Promotions {
    private final Map<String, Promotion> promotions;

    public Promotions() {
        this.promotions = new HashMap<>();
    }

    public void addPromotion(Promotion promotion) {
        promotions.put(promotion.getName(), promotion);
    }

    public Promotion findPromotionByName(String name) {
        return promotions.get(name);
    }
}
