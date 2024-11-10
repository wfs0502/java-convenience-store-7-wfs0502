package store.controller;

import store.model.ProductLoader;
import store.model.Products;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        ProductLoader productLoader = new ProductLoader();
        Products products = productLoader.load();

        outputView.printWelcomeMessage();
        outputView.printProducts(products);
    }
}
