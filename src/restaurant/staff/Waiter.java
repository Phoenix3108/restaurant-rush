package restaurant.staff;

import restaurant.customer.Customer;
import restaurant.customer.WaitingQueue;
import restaurant.floor.Table;
import restaurant.menu.Menu;
import restaurant.menu.Order;

/**
 * Does at most ONE of these per turn: seat, take order, or serve.
 * The Waiter acts before the Chef, so any order that is READY when the Waiter
 * acts was finished on an earlier turn.
 *
 * Every method returns ActionResult.fail(reason) for an invalid choice and
 * does NOT use up the task in that case.
 *
 * Owner: Part 3
 */
public class Waiter extends Staff {

    public Waiter() {
        super("Waiter");
    }

    /**
     * Seat a queued customer at a free table.
     * Fail if: already acted, customer not in queue, table occupied.
     * On success: remove from queue, table.assign(customer), customer.seatAt(table).
     */
    public ActionResult seat(Customer customer, Table table, WaitingQueue queue) {
        // TODO(Part 3)
        throw new UnsupportedOperationException("TODO Part 3: Waiter.seat");
    }

    /**
     * Take a seated customer's order. The CUSTOMER chooses the dish.
     * Fail if: already acted, customer not SEATED.
     * On success: item = customer.chooseMenuItem(menu);
     *             bill = customer.computeBillCents(item);
     *             order = new Order(customer, item, bill, currentTurn);
     *             customer.placeOrder(order);
     *             message should show the dish, e.g. "Customer #2 orders Pasta ($18.00)".
     */
    public ActionResult takeOrder(Customer customer, Menu menu, int currentTurn) {
        // TODO(Part 3)
        throw new UnsupportedOperationException("TODO Part 3: Waiter.takeOrder");
    }

    /**
     * Serve a READY order.
     * Fail if: already acted, order not READY.
     * On success: order.markServed(); order.getCustomer().serve(currentTurn).
     */
    public ActionResult serve(Order order, int currentTurn) {
        // TODO(Part 3)
        throw new UnsupportedOperationException("TODO Part 3: Waiter.serve");
    }
}
