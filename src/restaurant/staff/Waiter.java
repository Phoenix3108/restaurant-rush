package restaurant.staff;

import restaurant.customer.Customer;
import restaurant.customer.CustomerState;
import restaurant.customer.WaitingQueue;
import restaurant.floor.Table;
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
        if (hasActedThisTurn()){
            return ActionResult.fail("Waiter has already acted this turn");
        }
        if (customer==null || table==null){
            return ActionResult.fail("Must choose a customer and table");
        }        
        if (!queue.contains(customer)){
            return ActionResult.fail("Customer #" + customer.getId() + " is not in queue");
        }
        if (!table.isFree()){
            return ActionResult.fail("Table " + table.getId() + " is already occupied by Customer #" + table.getOccupant().getId());
        }
        queue.remove(customer);
        table.assign(customer);
        customer.seatAt(table);
        markActed();
        return ActionResult.ok("Waiter seated Customer #" + customer.getId() + " at Table " + table.getId());
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
        if (hasActedThisTurn()){
            return ActionResult.fail("Waiter has already acted this turn");
        }
        if (customer==null){
            return ActionResult.fail("Must choose a customer");
        }      
        if (customer.getState()!=CustomerState.SEATED){
            return ActionResult.fail("Customer #" + customer.getId() + " cannot order because the customer is " + customer.getState() + " not SEATED");
        }
        MenuItem item = customer.chooseMenuItem(menu);
        int bill = customer.computeBillCents(item);
        Order order = new Order(customer, item, bill, currentTurn);
        customer.placeOrder(order);
        markActed();
        return ActionResult.ok("Customer #" + customer.getId() + " orders " + item.getName() + " (" + Money.format(bill) + ")");
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
