package store.controller;

import java.util.Map;
import store.model.product.Products;
import store.model.order.ProductOrders;
import store.model.promotion.Promotions;
import store.service.OrderService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final Products products;
    private final Promotions promotions;
    private final OrderService orderService;

    public StoreController(InputView inputView, OutputView outputView, Products products, Promotions promotions) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.products = products;
        this.promotions = promotions;
        this.orderService = new OrderService(products, promotions, inputView, outputView);
    }

    public void run() {
        outputView.printWelcomeMessage();
        outputView.printProducts(products);

        while (true) {
            ProductOrders productOrders = promptForValidProductOrders();
            orderService.order(productOrders);
        }
    }

    private ProductOrders promptForValidProductOrders() {
        while (true) {
            try {
                Map<String, Integer> productOrders = inputView.readItem();
                return orderService.createProductOrders(productOrders);
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }
}
