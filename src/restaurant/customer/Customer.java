package restaurant.customer;

import restaurant.floor.Table;
import restaurant.menu.Menu;
import restaurant.menu.MenuItem;
import restaurant.menu.Order;

/**
 * A restaurant customer. The shared visit (WAITING -> SEATED -> ORDERED -> EATING
 * -> READY_TO_PAY -> PAID, or LEFT) lives here; each customer type is a subclass that
 * supplies its own behaviour by overriding a few methods:
 *
 *   getTypeLabel()              - "Regular", "VIP", "Critic" (for the display)
 *   getSatisfactionLossPerTurn() - abstract: every type must say how impatient it is
 *   computeBillCents(item)       - full price unless overridden (VIP: 10% off)
 *   chooseMenuItem(menu)         - the documented cycle rule unless overridden
 *   onServed(turn)               - hook called when the dish arrives (no-op by default)
 *
 * The controller and staff only ever call these through a Customer reference,
 * so they never need to check which type a customer is (brief 4.2).
 *
 * Owner: Part 2
 */
public abstract class Customer {

    public static final int MAX_SATISFACTION = 100;

    private final int id;
    private final int arrivalTurn;
    private int satisfaction;
    private CustomerState state;
    private Table table;
    private Order order;
    private int servedTurn = -1;
    private int readyToPayTurn = -1;

    protected Customer(int id, int arrivalTurn) {
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

    // ---------- behaviour each customer type supplies ----------

    /** Short name of this customer type for the display, e.g. "Regular" or "VIP". */
    public abstract String getTypeLabel();

    /** Satisfaction lost per waiting turn (Regular 8, VIP 5, Critic 12). */
    public abstract int getSatisfactionLossPerTurn();

    /** The bill for this item, in cents. Full price unless a type overrides it. */
    public int computeBillCents(MenuItem item) {
        return item.getPriceCents();
    }

    /**
     * Called once, right after this customer is served on the given turn.
     * Does nothing by default; a type that reacts to service (e.g. the Critic) overrides it.
     */
    protected void onServed(int currentTurn) {
    }

    /**
     * Changes satisfaction by delta, kept between 0 and MAX_SATISFACTION.
     * For subclasses that react to service (e.g. Critic +10 / -10).
     */
    protected final void adjustSatisfaction(int delta) {
        satisfaction = Math.max(0, Math.min(MAX_SATISFACTION, satisfaction + delta));
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

    /**
     * ORDERED -> EATING. Remember the turn so eating takes one full turn,
     * then let the customer type react to the service (onServed hook).
     */
    public void serve(int currentTurn) {
        requireState(CustomerState.ORDERED, "be served");
        this.servedTurn = currentTurn;
        this.state = CustomerState.EATING;
        onServed(currentTurn);
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
        return getTypeLabel() + " Customer #" + id + " [" + state + ", satisfaction " + satisfaction + "]";
    }
}
