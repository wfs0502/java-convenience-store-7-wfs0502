package store;

import store.controller.StoreController;
import store.model.product.ProductLoader;
import store.model.product.Products;
import store.model.promotion.PromotionLoader;
import store.model.promotion.Promotions;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        ProductLoader productLoader = new ProductLoader();
        Products products = productLoader.load();
        PromotionLoader promotionLoader = new PromotionLoader();
        Promotions promotions = promotionLoader.load();

        StoreController storeController = new StoreController(inputView, outputView, products, promotions);

        storeController.run();
    }
}
