package store.model.promotion;

public class Promotion {
    private final String name;
    private final int buyQuantity;
    private final int freeQuantity;
    private final String startDate;
    private final String endDate;

    public Promotion(String name, int buyQuantity, int freeQuantity, String startDate, String endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.freeQuantity = freeQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }
}
