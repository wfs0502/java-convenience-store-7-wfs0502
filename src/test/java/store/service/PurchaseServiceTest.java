package store.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.product.ProductLoader;
import store.model.product.Products;
import store.model.purchase.Purchase;

public class PurchaseServiceTest {
    private PurchaseService purchaseService;

    @BeforeEach
    void setUp() {
        ProductLoader productLoader = new ProductLoader();
        Products products = productLoader.load();

        purchaseService = new PurchaseService(products);
    }

    @Test
    void 정상적으로_구매가_이루어진다() {
        Map<String, Integer> items = new HashMap<>();
        items.put("콜라", 5);
        items.put("사이다", 10);
        Purchase purchase = new Purchase(items);

        assertThatCode(() -> purchaseService.purchase(purchase))
                .doesNotThrowAnyException();
    }

    @Test
    void 구매_수량이_재고_수량을_초과하면_오류가_발생한다() {
        Map<String, Integer> items = new HashMap<>();
        items.put("사이다", 17);
        Purchase purchase = new Purchase(items);

        assertThatThrownBy(() -> purchaseService.purchase(purchase))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
    }

    @Test
    void 존재하지_않는_상품을_입력하면_오류가_발생한다() {
        Map<String, Integer> items = new HashMap<>();
        items.put("포도주스", 2);
        Purchase purchase = new Purchase(items);

        assertThatThrownBy(() -> purchaseService.purchase(purchase))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }
}
