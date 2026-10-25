package restaurant.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.customer.Customer;
import restaurant.customer.WaitingQueue;
import restaurant.floor.Table;
import restaurant.menu.Menu;
import restaurant.menu.Order;
import restaurant.staff.Cashier;
import restaurant.staff.Chef;
import restaurant.staff.Waiter;

/**
 * Holds the whole game state: menu, tables, queue, staff, customers and scores.
 * Orders are reached through customers (customer.getOrder()), so there is one
 * source of truth.
 *
 * Owner: Part 4
 */
public class Restaurant {

    private final Menu menu;
    private final List<Table> tables = new ArrayList<>();
    private final WaitingQueue queue = new WaitingQueue();
    private final List<Customer> customers = new ArrayList<>();
    private final Waiter waiter = new Waiter();
    private final Chef chef = new Chef();
    private final Cashier cashier = new Cashier();

    private int revenueCents;
    private int paidCount;
    private int paidSatisfactionTotal;
    private int unhappyDepartures;

    public Restaurant(GameConfig config) {
        this.menu = Menu.standardMenu();
        for (int i = 1; i <= config.getNumTables(); i++) {
            tables.add(new Table(i));
        }
    }

    // ---------- getters ----------

    public Menu getMenu() {
        return menu;
    }

    public List<Table> getTables() {
        return Collections.unmodifiableList(tables);
    }

    public WaitingQueue getQueue() {
        return queue;
    }

    /** Every customer who has arrived so far, in arrival order. */
    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public Chef getChef() {
        return chef;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public int getRevenueCents() {
        return revenueCents;
    }

    public int getPaidCount() {
        return paidCount;
    }

    public int getUnhappyDepartures() {
        return unhappyDepartures;
    }

    public boolean hasPaidCustomers() {
        return paidCount > 0;
    }

    /** Average satisfaction recorded at payment. Only valid if hasPaidCustomers(). */
    public double getAveragePaidSatisfaction() {
        return paidCount == 0 ? 0 : (double) paidSatisfactionTotal / paidCount;
    }

    // ---------- updates ----------

    /** Step 1: new customer joins the queue. */
    public void addArrival(Customer customer) {
        customers.add(customer);
        queue.add(customer);
    }

    /** Add the bill to revenue and record the customer's satisfaction. */
    public void recordPayment(Customer customer) {
        // TODO(Part 4): revenueCents += order bill; paidCount++; paidSatisfactionTotal += satisfaction
        throw new UnsupportedOperationException("TODO Part 4: Restaurant.recordPayment");
    }

    /** Count an unhappy departure (customer.leave() handles the order and table). */
    public void recordDeparture(Customer customer) {
        // TODO(Part 4)
        throw new UnsupportedOperationException("TODO Part 4: Restaurant.recordDeparture");
    }

    // ---------- queries used by the controller to build the manager's options ----------

    public List<Table> getFreeTables() {
        // TODO(Part 4)
        throw new UnsupportedOperationException("TODO Part 4: Restaurant.getFreeTables");
    }

    /** Customers at a table whose order has not been taken (state SEATED). */
    public List<Customer> getCustomersAwaitingOrder() {
        // TODO(Part 4)
        throw new UnsupportedOperationException("TODO Part 4: Restaurant.getCustomersAwaitingOrder");
    }

    /** Orders the Chef can work on (IN_PREPARATION). */
    public List<Order> getOrdersInKitchen() {
        // TODO(Part 4)
        throw new UnsupportedOperationException("TODO Part 4: Restaurant.getOrdersInKitchen");
    }

    /** Orders the Waiter can serve (READY). */
    public List<Order> getReadyOrders() {
        // TODO(Part 4)
        throw new UnsupportedOperationException("TODO Part 4: Restaurant.getReadyOrders");
    }

    /** Customers still in the restaurant (not PAID, not LEFT). */
    public List<Customer> getActiveCustomers() {
        // TODO(Part 4)
        throw new UnsupportedOperationException("TODO Part 4: Restaurant.getActiveCustomers");
    }
}
