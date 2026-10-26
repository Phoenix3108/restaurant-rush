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
     * Documented rule: customers cycle through the menu by ID, so
     * #1 Salad, #2 Burger, #3 Pasta, #4 Salad, #5 Burger, #6 Pasta.
     * Deterministic, so every run of the base setting is reproducible.
     */
    public MenuItem chooseMenuItem(Menu menu) {
        return menu.getItem((id - 1) % menu.size());
    }

    // ---------- state changes ----------

    /** True while WAITING, SEATED or ORDERED (not served yet). */
    public boolean isLosingSatisfaction() {
        return state == CustomerState.WAITING
                || state == CustomerState.SEATED
                || state == CustomerState.ORDERED;
    }

    /**
     * Step 5: lose getSatisfactionLossPerTurn() points (minimum 0) if not served yet.
     * Uses the overridable loss rate, so Stage 2 VIP/Critic need no changes here.
     *
     * @return true if satisfaction reached 0, so the customer must leave
     */
    public boolean applyWaitingPenalty() {
        if (!isLosingSatisfaction()) {
            return false;
        }
        satisfaction = Math.max(0, satisfaction - getSatisfactionLossPerTurn());
        return satisfaction == 0;
    }

    /** WAITING -> SEATED at the given table. */
    public void seatAt(Table table) {
        requireState(CustomerState.WAITING, "be seated");
        this.table = table;
        this.state = CustomerState.SEATED;
    }

    /** SEATED -> ORDERED with the given order. */
    public void placeOrder(Order order) {
        requireState(CustomerState.SEATED, "place an order");
        this.order = order;
        this.state = CustomerState.ORDERED;
    }

    /** ORDERED -> EATING. Remember the turn so eating takes one full turn. */
    public void serve(int currentTurn) {
        requireState(CustomerState.ORDERED, "be served");
        this.servedTurn = currentTurn;
        this.state = CustomerState.EATING;
    }

    /**
     * Step 4: a customer served on an EARLIER turn finishes eating and becomes READY_TO_PAY.
     *
     * @return true if the customer became READY_TO_PAY this call
     */
    public boolean finishEatingIfDone(int currentTurn) {
        if (state == CustomerState.EATING && servedTurn < currentTurn) {
            state = CustomerState.READY_TO_PAY;
            readyToPayTurn = currentTurn;
            return true;
        }
        return false;
    }

    /** READY_TO_PAY on an EARLIER turn, so the Cashier may charge them this turn. */
    public boolean canPay(int currentTurn) {
        return state == CustomerState.READY_TO_PAY && readyToPayTurn < currentTurn;
    }

    /** READY_TO_PAY -> PAID. Satisfaction stays at its current value (it is the recorded score). */
    public void markPaid() {
        requireState(CustomerState.READY_TO_PAY, "pay");
        this.state = CustomerState.PAID;
    }

    /** -> LEFT. Cancel any unfinished order and free the table. */
    public void leave() {
        if (!isLosingSatisfaction()) {
            throw new IllegalStateException(this + " cannot leave: already served or gone");
        }
        state = CustomerState.LEFT;
        if (order != null) {
            order.cancel();      // only ORDERED customers have one, and it is not served yet
        }
        if (table != null) {
            table.release();
        }
    }

    /** Guards every state change so a wrong call fails loudly instead of corrupting the game. */
    private void requireState(CustomerState expected, String action) {
        if (state != expected) {
            throw new IllegalStateException(
                    this + " cannot " + action + " (must be " + expected + ")");
        }
    }

    @Override
    public String toString() {
        return "Customer #" + id + " [" + state + ", satisfaction " + satisfaction + "]";
    }
}