package restaurant.customer;

import restaurant.menu.MenuItem;
import restaurant.menu.Money;

/**
 * A VIP customer: more patient (loses 5 satisfaction per waiting turn instead of 8)
 * and gets 10% off the bill. The discount is applied when the order is taken, because
 * Waiter.takeOrder() locks the bill using computeBillCents().
 *
 * Owner: Part 2
 */
public class VIPCustomer extends Customer {

    private static final int LOSS_PER_TURN = 5;
    private static final int DISCOUNT_PERCENT = 10;

    public VIPCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    public String getTypeLabel() {
        return "VIP";
    }

    @Override
    public int getSatisfactionLossPerTurn() {
        return LOSS_PER_TURN;
    }

    /**
     * Full price minus 10%, rounded to the nearest cent (Burger $15.00 -> $13.50).
     * Uses Money.percentOff so every discount (VIP, Happy Hour) shares one rounding rule.
     */
    @Override
    public int computeBillCents(MenuItem item) {
        return Money.percentOff(item.getPriceCents(), DISCOUNT_PERCENT);
    }
}
