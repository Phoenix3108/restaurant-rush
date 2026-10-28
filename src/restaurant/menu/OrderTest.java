package restaurant.menu;

import restaurant.customer.Customer;

public class OrderTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        testSaladPreparation();
        testPastaPreparation();
        testServingAndPayment();
        testCancellation();
        testInvalidTransitions();
        testOrderIds();
        testCancelAfterServing();
        testPrepareAfterCancellation();
        testBillAndReferences();

        System.out.println("\n========================");
        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);
        System.out.println("========================");

        if (failed > 0) {
            throw new AssertionError("Some tests failed.");
        }
    }

    private static void testSaladPreparation() {
        Order order = createOrder("Salad", 900, 1);

        check(order.getStatus() == OrderStatus.IN_PREPARATION,
                "New order starts in preparation");

        check(order.getUnitsRemaining() == 1,
                "Salad initially needs one unit");

        boolean becameReady = order.advancePreparation();

        check(becameReady,
                "Salad becomes ready after one unit");

        check(order.getStatus() == OrderStatus.READY,
                "Salad status becomes READY");

        check(order.getUnitsRemaining() == 0,
                "Salad has no remaining units");
    }

    private static void testPastaPreparation() {
        Order order = createOrder("Pasta", 1800, 2);

        boolean firstAction = order.advancePreparation();

        check(!firstAction,
                "Pasta is not ready after the first unit");

        check(order.getStatus() == OrderStatus.IN_PREPARATION,
                "Pasta remains in preparation after one unit");

        boolean secondAction = order.advancePreparation();

        check(secondAction,
                "Pasta becomes ready after the second unit");

        check(order.getStatus() == OrderStatus.READY,
                "Pasta status becomes READY");
    }

    private static void testServingAndPayment() {
        Order order = createOrder("Salad", 900, 1);

        order.advancePreparation();
        order.markServed();

        check(order.getStatus() == OrderStatus.SERVED,
                "Ready order can be served");

        order.markPaid();

        check(order.getStatus() == OrderStatus.PAID,
                "Served order can be paid");
    }

    private static void testCancellation() {
        Order preparingOrder = createOrder("Salad", 900, 1);

        preparingOrder.cancel();

        check(preparingOrder.getStatus() == OrderStatus.CANCELLED,
                "Order in preparation can be cancelled");

        Order readyOrder = createOrder("Salad", 900, 1);

        readyOrder.advancePreparation();
        readyOrder.cancel();

        check(readyOrder.getStatus() == OrderStatus.CANCELLED,
                "Ready order can be cancelled");
    }

    private static void testInvalidTransitions() {
        Order order = createOrder("Salad", 900, 1);

        checkThrows(order::markServed,
                "Cannot serve an order still in preparation");

        checkThrows(order::markPaid,
                "Cannot pay for an unserved order");

        order.advancePreparation();
        order.markServed();
        order.markPaid();

        checkThrows(order::markPaid,
                "Cannot pay for an already paid order");

        checkThrows(order::cancel,
                "Cannot cancel a paid order");

        checkThrows(order::advancePreparation,
                "Cannot prepare an order after it is ready and paid");
    }

    private static Order createOrder(
            String name, int priceCents, int prepUnits) {

        MenuItem item = new MenuItem(name, priceCents, prepUnits);

        /*
         * This assumes Customer has a constructor Customer(int id).
         * If your Customer class uses a different constructor,
         * adjust this line to match it.
         */
        Customer customer = new Customer(1, 1);

        return new Order(customer, item, priceCents, 1);
    }

    private static void check(boolean condition, String description) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + description);
        } else {
            failed++;
            System.out.println("FAIL: " + description);
        }
    }

    private static void checkThrows(
            Runnable action, String description)
    {

        try {
            action.run();
            check(false, description);
        } catch (IllegalStateException e) {
            check(true, description);
        }
    }

    private static void testOrderIds() {
        Order first = createOrder("Salad", 900, 1);
        Order second = createOrder("Burger", 1500, 1);

        check(second.getId() == first.getId() + 1,
                "Order IDs increment by one");

        check(second.getId() != first.getId(),
                "Each order receives a unique ID");
    }

    private static void testCancelAfterServing() {
        Order order = createOrder("Salad", 900, 1);

        order.advancePreparation();
        order.markServed();

        checkThrows(order::cancel,
                "Cannot cancel an order after serving");
    }

    private static void testPrepareAfterCancellation() {
        Order order = createOrder("Salad", 900, 1);

        order.cancel();

        checkThrows(order::advancePreparation,
                "Cannot prepare a cancelled order");

    }

    private static void testBillAndReferences() {
        Customer customer = new Customer(99, 1);
        MenuItem item = new MenuItem("Burger", 1500, 1);

        Order order = new Order(customer, item, 1500, 1);

        check(order.getCustomer() == customer,
                "Order retains the correct customer");

        check(order.getItem() == item,
                "Order retains the correct menu item");

        order.advancePreparation();
        order.markServed();
        order.markPaid();

        check(order.getBillCents() == 1500,
                "Bill remains fixed after payment");

    }
}