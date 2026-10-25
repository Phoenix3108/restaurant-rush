package restaurant.staff;

/**
 * Outcome of a staff action. A failed action carries the reason, so the UI can
 * explain it and let the manager choose again without using up the task (brief 3.4).
 *
 * Owner: Part 3 (shared by everyone)
 */
public final class ActionResult {

    private final boolean success;
    private final String message;

    private ActionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static ActionResult ok(String message) {
        return new ActionResult(true, message);
    }

    public static ActionResult fail(String reason) {
        return new ActionResult(false, reason);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
