package restaurant.staff;

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

    /** Called by the controller at the start of every turn. */
    public void startNewTurn() {
        actedThisTurn = false;
    }
}
