package restaurant.customer;

import restaurant.menu.Menu;
import restaurant.menu.MenuItem;

/**
 * A food critic: impatient (loses 12 satisfaction per waiting turn instead of 8),
 * always orders the most expensive item on the menu (Pasta on the base menu),
 * and reacts to how quickly they were served.
 *
 * Reaction rule (keep in sync with README "Documented rules"):
 *   when served, +10 satisfaction if served as fast as the kitchen allows
 *   (serve turn - order turn <= the dish's preparation units), otherwise -10.
 *   The bonus only goes to a manager who prioritises the Critic's dish
 *   (Pasta on the base menu: 2 turns).
 *   Satisfaction stays within 0..100 (adjustSatisfaction clamps it).
 *   A served Critic never leaves, even at 0 - they still eat and pay.
 *
 * Pays full price, so computeBillCents is NOT overridden.
 *
 * Owner: Part 3
 */
public class CriticCustomer extends Customer {

private static final int LOSS_PER_TURN = 12;
    private static final int REACTION_POINTS = 10;

    public CriticCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    /** Shown in the display, e.g. "Critic Customer #3 [...]". */
    @Override
    public String getTypeLabel() {
        return "Critic";
    }

    @Override
    public int getSatisfactionLossPerTurn() {
        return LOSS_PER_TURN;
    }

    /**
     * Ignores the cycling rule and always orders the most expensive item
     * (Pasta on the base menu). Uses Menu.getMostExpensive() from Part 1.
     */
    @Override
    public MenuItem chooseMenuItem(Menu menu) {
        return menu.getMostExpensive();
    }

    /**
     * Customer.serve(currentTurn) calls this after the state becomes EATING,
     * so Waiter never needs to know this is a Critic.
     * Judges kitchen and serving.
     * Wait is counted from when the order was taken,
     * not from arrival since time in the queue
     * is already punished by the 12-per-turn loss.
     */
    @Override
    protected void onServed(int currentTurn) {
        int turnsToServe = currentTurn - getOrder().getTurnTaken();
        int fastest = getOrder().getItem().getPrepUnits();
        if (turnsToServe <= fastest) {
            adjustSatisfaction(+REACTION_POINTS);
        } else {
            adjustSatisfaction(-REACTION_POINTS);
        }
    }
}