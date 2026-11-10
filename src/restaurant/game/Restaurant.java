package restaurant.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.customer.Customer;
import restaurant.customer.CustomerState;
import restaurant.customer.WaitingQueue;
import restaurant.floor.Table;
import restaurant.menu.HappyHour;
import restaurant.menu.Menu;
import restaurant.menu.Order;
import restaurant.menu.OrderStatus;
import restaurant.staff.Cashier;
import restaurant.staff.Chef;
import restaurant.staff.Host;
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
    private final Host host = new Host();
    private final HappyHour happyHour = new HappyHour();

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

    public Host getHost() {
        return host;
    }

    public HappyHour getHappyHour() {
        return happyHour;
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

    /** Customers whose dish has been served so far (brief 3.5), whether or not they have paid yet. */
    public int getServedCount() {
        int count = 0;
        for (Customer customer : customers) {
            if (customer.getServedTurn() != -1) {
                count++;
            }
        }
        return count;
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
        revenueCents += customer.getOrder().getBillCents();
        paidCount++;
        paidSatisfactionTotal += customer.getSatisfaction();
    }

    /** Count an unhappy departure (customer.leave() handles the order and table). */
    public void recordDeparture(Customer customer) {
        queue.remove(customer);
        unhappyDepartures++;
    }

    // ---------- queries used by the controller to build the manager's options ----------

    public List<Table> getFreeTables() {
        List<Table> free = new ArrayList<>();
        for (Table table : tables) {
            if (table.isFree()) {
                free.add(table);
            }
        }
        return free;
    }

    /** Customers at a table whose order has not been taken (state SEATED). */
    public List<Customer> getCustomersAwaitingOrder() {
        return customersIn(CustomerState.SEATED);
    }

    /** Orders the Chef can work on (IN_PREPARATION). */
    public List<Order> getOrdersInKitchen() {
        return ordersWithStatus(OrderStatus.IN_PREPARATION);
    }

    /** Orders the Waiter can serve (READY). */
    public List<Order> getReadyOrders() {
        return ordersWithStatus(OrderStatus.READY);
    }

    /** Customers still in the restaurant (not PAID, not LEFT). */
    public List<Customer> getActiveCustomers() {
        List<Customer> active = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getState() != CustomerState.PAID && customer.getState() != CustomerState.LEFT) {
                active.add(customer);
            }
        }
        return active;
    }

    /** Customers the Cashier could charge this turn (finished eating on an earlier turn). */
    public List<Customer> getCustomersAbleToPay(int currentTurn) {
        List<Customer> result = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.canPay(currentTurn)) {
                result.add(customer);
            }
        }
        return result;
    }

    private List<Customer> customersIn(CustomerState state) {
        List<Customer> result = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getState() == state) {
                result.add(customer);
            }
        }
        return result;
    }

    /** Orders are reached through customers, oldest customer first. */
    private List<Order> ordersWithStatus(OrderStatus status) {
        List<Order> result = new ArrayList<>();
        for (Customer customer : customers) {
            Order order = customer.getOrder();
            if (order != null && order.getStatus() == status) {
                result.add(order);
            }
        }
        return result;
    }
}
