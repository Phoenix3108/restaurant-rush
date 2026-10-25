package restaurant.customer;

/**
 * Where a customer is in their visit.
 *
 * WAITING -> SEATED -> ORDERED -> EATING -> READY_TO_PAY -> PAID
 * WAITING / SEATED / ORDERED -> LEFT (satisfaction reached 0)
 *
 * Owner: Part 2
 */
public enum CustomerState {
    /** In the waiting queue, no table yet. Loses satisfaction. */
    WAITING,
    /** At a table, order not taken yet. Loses satisfaction. */
    SEATED,
    /** Order taken, not served yet. Loses satisfaction. */
    ORDERED,
    /** Food served, eating. No satisfaction loss. */
    EATING,
    /** Finished eating, waiting for the Cashier. No satisfaction loss. */
    READY_TO_PAY,
    /** Paid and gone. Satisfaction at payment is recorded. */
    PAID,
    /** Left unhappy without paying. */
    LEFT
}
