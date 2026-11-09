package restaurant.staff;

import restaurant.customer.Customer;
import restaurant.customer.CustomerState;
import restaurant.customer.WaitingQueue;
import restaurant.floor.Table;
import restaurant.menu.HappyHour;
import restaurant.menu.Menu;
import restaurant.menu.MenuItem;
import restaurant.menu.Order;
import restaurant.menu.Money;

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
        return seatFromQueue(customer, table, queue);
    }

    /**
     * Take a seated customer's order with no Happy Hour (kept so existing callers still work).
     * Same as takeOrder(customer, menu, currentTurn, happyHour) with a Happy Hour that is never active.
     */
    public ActionResult takeOrder(Customer customer, Menu menu, int currentTurn) {
        return takeOrder(customer, menu, currentTurn, new HappyHour());
    }

    /**
     * Take a seated customer's order. The CUSTOMER chooses the dish.
     * Fail if: already acted, customer not SEATED.
     * Bill = customer's own price (VIP 10% off) and then Happy Hour 20% off if active,
     * locked into the Order now, so it does not change when Happy Hour ends.
     * Message shows the dish, e.g. "Customer #2 orders Burger ($10.80, Happy Hour)".
     */
    public ActionResult takeOrder(Customer customer, Menu menu, int currentTurn, HappyHour happyHour) {
        if (hasActedThisTurn()) {
            return ActionResult.fail("Waiter has already acted this turn");
        }
        if (customer == null) {
            return ActionResult.fail("Must choose a customer");
        }
        if (customer.getState() != CustomerState.SEATED) {
            return ActionResult.fail("Customer #" + customer.getId() + " cannot order because the customer is "
                    + customer.getState() + " not SEATED");
        }
        MenuItem item = customer.chooseMenuItem(menu);
        int bill = happyHour.apply(customer.computeBillCents(item), currentTurn);
        Order order = new Order(customer, item, bill, currentTurn);
        customer.placeOrder(order);
        markActed();
        String happyHourNote = happyHour.isActive(currentTurn) ? ", Happy Hour" : "";
        return ActionResult.ok("Customer #" + customer.getId() + " orders " + item.getName()
                + " (" + Money.format(bill) + happyHourNote + ")");
    }

    /**
     * Serve a READY order.
     * Fail if: already acted, order not READY.
     * On success: order.markServed(); order.getCustomer().serve(currentTurn).
     */
    public ActionResult serve(Order order, int currentTurn) {
        if (hasActedThisTurn()){
            return ActionResult.fail("Waiter has already acted this turn");
        }
        if (order==null){
            return ActionResult.fail("Must choose an order");
        }  
        if (!order.isReady()){
            return ActionResult.fail("Order #" + order.getId() + " is " + order.getStatus() + ", not ready");
        }
        order.markServed();
        order.getCustomer().serve(currentTurn);
        markActed();
        return ActionResult.ok("Waiter served " + order.getItem().getName() + " to Customer #" + order.getCustomer().getId());
    }
}