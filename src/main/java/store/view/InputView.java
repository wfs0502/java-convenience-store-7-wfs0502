package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.HashMap;
import java.util.Map;

public class InputView {
    private static final String PURCHASE_PROMPT = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String FORMAT_ERROR = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";

    public Map<String, Integer> readItem() {
        System.out.println(PURCHASE_PROMPT);
        String input = Console.readLine();
        return parseItems(input);
    }

    private Map<String, Integer> parseItems(String input) {
        Map<String, Integer> itemMap = new HashMap<>();
        String[] items = input.split("\\s*,\\s*");

        for (String item : items) {
            String[] parts = validateAndParseItem(item);
            String productName = parts[0];
            int quantity = Integer.parseInt(parts[1]);
            itemMap.put(productName, quantity);
        }
        return itemMap;
    }

    private String[] validateAndParseItem(String item) {
        if (!item.startsWith("[") || !item.endsWith("]") || !item.contains("-")) {
            throw new IllegalArgumentException(FORMAT_ERROR);
        }
        String trimmedItem = item.substring(1, item.length() - 1);
        String[] parts = trimmedItem.split("-");

        if (parts.length != 2 || !isValidQuantity(parts[1])) {
            throw new IllegalArgumentException(FORMAT_ERROR);
        }
        return parts;
    }

    private boolean isValidQuantity(String quantity) {
        try {
            return Integer.parseInt(quantity) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
