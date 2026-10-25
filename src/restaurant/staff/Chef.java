package restaurant.staff;

import restaurant.menu.Order;

/**
 * Advances at most ONE order by ONE preparation unit per turn.
 * May work on an order the Waiter took earlier this same turn.
 *
 * Owner: Part 3
 */
public class Chef extends Staff {

    public Chef() {
        super("Chef");
    }

    /**
     * Fail if: already acted, order not IN_PREPARATION.
     * On success: order.advancePreparation(); message says if it is now READY.
     */
    public ActionResult prepare(Order order) {
        // TODO(Part 3)
        throw new UnsupportedOperationException("TODO Part 3: Chef.prepare");
    }
}
