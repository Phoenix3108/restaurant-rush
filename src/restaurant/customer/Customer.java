package restaurant.customer;

import restaurant.floor.Table;
import restaurant.menu.Menu;
import restaurant.menu.MenuItem;
import restaurant.menu.Order;

/**
 * A restaurant customer. Stage 1 has only regular customers; Stage 2 will add
 * VIP and Critic subclasses that override the satisfaction loss and billing methods,
 * so keep those behaviours in this class (not in the controller).
 *
 * Owner: Part 2
 */
public class Customer {

    public static final int MAX_SATISFACTION = 100;
    private static final int REGULAR_LOSS_PER_TURN = 8;

    private final int id;
    private final int arrivalTurn;
    private int satisfaction;
    private CustomerState state;
    private Table table;
    private Order order;
    private int servedTurn = -1;
    private int readyToPayTurn = -1;

    public Customer(int id, int arrivalTurn) {
        this.id = id;
        this.arrivalTurn = arrivalTurn;
        this.satisfaction = MAX_SATISFACTION;
        this.state = CustomerState.WAITING;
    }

    // ---------- getters ----------

    public int getId() {
        return id;
    }

    public int getArrivalTurn() {
        return arrivalTurn;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerState getState() {
        return state;
    }

    public Table getTable() {
        return table;
    }

    public Order getOrder() {
        return order;
    }

    public int getServedTurn() {
        return servedTurn;
    }

    public int getReadyToPayTurn() {
        return readyToPayTurn;
    }

    // ---------- behaviour that Stage 2 subclasses will override ----------

    /** Satisfaction lost per waiting turn. Regular = 8 (VIP 5, Critic 12 in Stage 2). */
    public int getSatisfactionLossPerTurn() {
        return REGULAR_LOSS_PER_TURN;
    }

    /** The bill for this item. Regular pays full price (VIP gets 10% off in Stage 2). */
    public int computeBillCents(MenuItem item) {
        return item.getPriceCents();
    }

    /**
     * The customer picks their own dish (the manager never chooses).
     * Must be deterministic or seeded random, and documented in the README.
     */
    public MenuItem chooseMenuItem(Menu menu) {
        // TODO(Part 2): e.g. menu.getItem((id - 1) % menu.size()), document the rule
        throw new UnsupportedOperationException("TODO Part 2: Customer.chooseMenuItem");
    }

    // ---------- state changes ----------

    /** True while WAITING, SEATED or ORDERED (not served yet). */
    public boolean isLosingSatisfaction() {
        // TODO(Part 2)
        throw new UnsupportedOperationException("TODO Part 2: Customer.isLosingSatisfaction");
    }

    /**
     * Step 5: lose getSatisfactionLossPerTurn() points (minimum 0) if not served yet.
     *
     * @return true if satisfaction reached 0, so the customer must leave
     */
    public boolean applyWaitingPenalty() {
        // TODO(Part 2)
        throw new UnsupportedOperationException("TODO Part 2: Customer.applyWaitingPenalty");
    }

    /** WAITING -> SEATED at the given table. */
    public void seatAt(Table table) {
        // TODO(Part 2)
        throw new UnsupportedOperationException("TODO Part 2: Customer.seatAt");
    }

    /** SEATED -> ORDERED with the given order. */
    public void placeOrder(Order order) {
        // TODO(Part 2)
        throw new UnsupportedOperationException("TODO Part 2: Customer.placeOrder");
    }

    /** ORDERED -> EATING. Remember the turn so eating takes one full turn. */
    public void serve(int currentTurn) {
        // TODO(Part 2)
        throw new UnsupportedOperationException("TODO Part 2: Customer.serve");
    }

    /**
     * Step 4: a customer served on an EARLIER turn finishes eating and becomes READY_TO_PAY.
     *
     * @return true if the customer became READY_TO_PAY this call
     */
    public boolean finishEatingIfDone(int currentTurn) {
        // TODO(Part 2): EATING && servedTurn < currentTurn -> READY_TO_PAY, readyToPayTurn = currentTurn
        throw new UnsupportedOperationException("TODO Part 2: Customer.finishEatingIfDone");
    }

    /** READY_TO_PAY on an EARLIER turn, so the Cashier may charge them this turn. */
    public boolean canPay(int currentTurn) {
        // TODO(Part 2): state == READY_TO_PAY && readyToPayTurn < currentTurn
        throw new UnsupportedOperationException("TODO Part 2: Customer.canPay");
    }

    /** READY_TO_PAY -> PAID. Satisfaction stays at its current value (it is the recorded score). */
    public void markPaid() {
        // TODO(Part 2)
        throw new UnsupportedOperationException("TODO Part 2: Customer.markPaid");
    }

    /** -> LEFT. Cancel any unfinished order and free the table. */
    public void leave() {
        // TODO(Part 2): state = LEFT; if order not served -> order.cancel(); if table != null -> table.release()
        throw new UnsupportedOperationException("TODO Part 2: Customer.leave");
    }

    @Override
    public String toString() {
        return "Customer #" + id + " [" + state + ", satisfaction " + satisfaction + "]";
    }
}
