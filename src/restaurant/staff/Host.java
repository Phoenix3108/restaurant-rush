package restaurant.staff;

import restaurant.customer.Customer;
import restaurant.customer.WaitingQueue;
import restaurant.floor.Table;

/**
 * Stage 3: seats at most ONE queued customer per turn, in addition to the
 * Waiter's task (the Waiter can still seat customers too). Uses the same
 * seating rules as the Waiter, shared in Staff.seatFromQueue().
 *
 * Owner: Part 3
 */
public class Host extends Staff {

    public Host() {
        super("Host");
    }

    /**
     * Seat a queued customer at a free table.
     * Fail if: already acted, customer not in queue, table occupied.
     */
    public ActionResult seat(Customer customer, Table table, WaitingQueue queue) {
        return seatFromQueue(customer, table, queue);
    }
}