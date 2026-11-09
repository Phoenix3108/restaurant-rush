package restaurant.staff;

import restaurant.customer.Customer;
import restaurant.customer.WaitingQueue;
import restaurant.floor.Table;

/**
 * Common base for staff who can do one task per turn.
 * (Stage 3 adds a Host, which also extends this.)
 *
 * Owner: Part 3
 */
public abstract class Staff {

    private final String role;
    private boolean actedThisTurn;

    protected Staff(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public boolean hasActedThisTurn() {
        return actedThisTurn;
    }

    /** Call only after an action SUCCEEDS. A failed action must not use up the task. */
    protected void markActed() {
        actedThisTurn = true;
    }

    /**
     * Shared seating rules for every staff member who can seat customers (Waiter, Host).
     * Fails, without using up the task, if: already acted, customer not in queue,
     * table occupied. On success: remove from queue, table.assign, customer.seatAt.
     */
    protected ActionResult seatFromQueue(Customer customer, Table table, WaitingQueue queue) {
        if (hasActedThisTurn()) {
            return ActionResult.fail(role + " has already acted this turn");
        }
        if (customer == null || table == null) {
            return ActionResult.fail("Must choose a customer and table");
        }
        if (!queue.contains(customer)) {
            return ActionResult.fail("Customer #" + customer.getId() + " is not in queue");
        }
        if (!table.isFree()) {
            return ActionResult.fail("Table " + table.getId() + " is already occupied by Customer #"
                    + table.getOccupant().getId());
        }
        queue.remove(customer);
        table.assign(customer);
        customer.seatAt(table);
        markActed();
        return ActionResult.ok(role + " seated Customer #" + customer.getId() + " at Table " + table.getId());
    }

    /** Called by the controller at the start of every turn. */
    public void startNewTurn() {
        actedThisTurn = false;
    }
}