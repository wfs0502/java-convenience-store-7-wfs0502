package store;

import store.controller.StoreController;
import store.model.ProductLoader;
import store.model.Products;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        ProductLoader productLoader = new ProductLoader();
        Products products = productLoader.load();

        StoreController storeController = new StoreController(inputView, outputView, products);

        storeController.run();
    }
}
