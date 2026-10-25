package restaurant.menu;

/**
 * Money helpers. All amounts are stored as integer cents (brief 2.2)
 * and only converted to dollars for display.
 *
 * Owner: Part 1 (shared by everyone)
 */
public final class Money {

    private Money() {
    }

    /** Converts whole dollars to cents, e.g. dollars(18) == 1800. */
    public static int dollars(int wholeDollars) {
        return wholeDollars * 100;
    }

    /** Formats cents for display, e.g. 2430 -> "$24.30". */
    public static String format(int cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }
}
