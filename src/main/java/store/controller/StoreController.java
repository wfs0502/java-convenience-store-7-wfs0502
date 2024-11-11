package store.controller;

import java.util.Map;
import store.model.product.Products;
import store.model.purchase.Purchase;
import store.service.PurchaseService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final Products products;
    private final PurchaseService purchaseService;

    public StoreController(InputView inputView, OutputView outputView, Products products) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.products = products;
        this.purchaseService = new PurchaseService(products);
    }

    public void run() {
        outputView.printWelcomeMessage();
        outputView.printProducts(products);

        while (true) {
            Purchase purchase = new Purchase(promptForPurchaseItems());
            processPurchase(purchase);
        }
    }

    private Map<String, Integer> promptForPurchaseItems() {
        while (true) {
            try {
                return inputView.readItem();
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private void processPurchase(Purchase items) {
        try {
            purchaseService.purchase(items);
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e.getMessage());
        }
    }
}
