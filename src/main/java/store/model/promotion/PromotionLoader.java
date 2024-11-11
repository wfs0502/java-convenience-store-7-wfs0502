package store.model.promotion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PromotionLoader {
    private final static String FILE_PATH = "src/main/resources/promotions.md";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Promotions load() {
        Promotions promotions = new Promotions();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            reader.readLine(); // 파일의 첫번째 줄은 건너뜀
            reader.lines().map(this::parsePromotion).forEach(promotions::addPromotion);

            return promotions;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Promotion parsePromotion(String line) {
        String[] fields = line.split(",");
        String name = fields[0];
        int buyQuantity = Integer.parseInt(fields[1]);
        int freeQuantity = Integer.parseInt(fields[2]);
        LocalDate startDate = LocalDate.parse(fields[3], DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(fields[4], DATE_FORMATTER);

        return new Promotion(name, buyQuantity, freeQuantity, startDate, endDate);
    }
}
