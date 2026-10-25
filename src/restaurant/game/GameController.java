package restaurant.game;

import restaurant.customer.Customer;
import restaurant.ui.ConsoleUI;

/**
 * Runs the game: 18 turns, each with the six steps from brief 3.2, then the result.
 * All game rules live here and in the domain classes, never in ConsoleUI.
 *
 * Owner: Part 4
 */
public class GameController {

    private final GameConfig config;
    private final ConsoleUI ui;
    private final Restaurant restaurant;
    private int nextCustomerId = 1;

    public GameController(GameConfig config, ConsoleUI ui) {
        this.config = config;
        this.ui = ui;
        this.restaurant = new Restaurant(config);
    }

    /** Plays all turns (no early ending), then shows Victory or Defeat. */
    public void run() {
        for (int turn = 1; turn <= config.getMaxTurns(); turn++) {
            playTurn(turn);
        }
        ui.showFinalResult(isVictory(), restaurant, config);
    }

    private void playTurn(int turn) {
        restaurant.getWaiter().startNewTurn();
        restaurant.getChef().startNewTurn();
        restaurant.getCashier().startNewTurn();

        ui.showTurnHeader(turn, config.getMaxTurns());
        handleArrivals(turn);                // step 1
        ui.showTurnStart(turn, restaurant);   // shown after arrival so the manager sees the new customer
        handleWaiterChoice(turn);             // step 2 + 3: Waiter acts first...
        handleChefChoice(turn);               // ...then Chef
        handleEatingAndPayment(turn);         // step 4
        handleWaitingAndAbandonment(turn);    // step 5
        ui.showTurnEnd(turn, restaurant);
    }

    /** Step 1: the scheduled customer joins the queue with satisfaction 100. */
    private void handleArrivals(int turn) {
        if (config.isArrivalTurn(turn)) {
            Customer customer = new Customer(nextCustomerId++, turn);
            restaurant.addArrival(customer);
            ui.showMessage(customer + " arrives and joins the queue.");
        }
    }

    /**
     * Step 2/3 (Waiter): build options (seat X at table Y / take order of X / serve order Z / Wait),
     * ask ui.askChoice(...), call the Waiter method. If the result failed, ui.showError(reason)
     * and ask again (the task is not used up).
     */
    private void handleWaiterChoice(int turn) {
        // TODO(Part 4)
    }

    /** Step 2/3 (Chef): options = each order in the kitchen, or Wait. Same retry loop as the Waiter. */
    private void handleChefChoice(int turn) {
        // TODO(Part 4)
    }

    /**
     * Step 4: for each active customer, customer.finishEatingIfDone(turn);
     * then Customer paid = cashier.collectPayment(...); if paid != null, restaurant.recordPayment(paid).
     */
    private void handleEatingAndPayment(int turn) {
        // TODO(Part 4)
    }

    /**
     * Step 5: every active customer applies the waiting penalty; anyone who hits 0 calls
     * customer.leave() (also remove from queue if waiting) and restaurant.recordDeparture(...).
     */
    private void handleWaitingAndAbandonment(int turn) {
        // TODO(Part 4)
    }

    /** Step 6: all three targets from GameConfig must be met. */
    private boolean isVictory() {
        // TODO(Part 4)
        return false;
    }
}
