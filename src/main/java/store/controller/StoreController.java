package store.controller;

import java.util.Map;
import store.model.product.Products;
import store.model.order.ProductOrders;
import store.service.OrderService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final Products products;
    private final OrderService orderService;

    public StoreController(InputView inputView, OutputView outputView, Products products) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.products = products;
        this.orderService = new OrderService(products);
    }

    public void run() {
        outputView.printWelcomeMessage();
        outputView.printProducts(products);

        while (true) {
            ProductOrders productOrders = createProductOrders();
            orderService.order(productOrders);
        }
    }

    private Map<String, Integer> promptForOrderItems() {
        while (true) {
            try {
                return inputView.readItem();
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private ProductOrders createProductOrders() {
        while (true) {
            try {
                Map<String, Integer> orderItems = promptForOrderItems();
                return orderService.createProductOrders(orderItems);
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }
}
