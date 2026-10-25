package restaurant.menu;

/**
 * Lifecycle of an order.
 *
 * IN_PREPARATION -> READY -> SERVED -> PAID
 * IN_PREPARATION or READY -> CANCELLED (customer left before being served)
 *
 * Owner: Part 1
 */
public enum OrderStatus {
    /** Taken by the Waiter; the Chef still has preparation units to do. */
    IN_PREPARATION,
    /** All preparation units done; waiting for the Waiter to serve it. */
    READY,
    /** Served to the customer. */
    SERVED,
    /** Bill collected by the Cashier. */
    PAID,
    /** Customer left before being served. Never counts toward revenue. */
    CANCELLED
}
