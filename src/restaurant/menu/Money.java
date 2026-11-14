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

    /**
     * Applies a percentage discount and rounds to the nearest cent.
     */
    public static int percentOff(int cents, int percent) {
        if (cents < 0 || percent < 0 || percent > 100) {
            throw new IllegalArgumentException(
                    "Invalid amount or discount percentage.");
        }

        return (int) (((long) cents * (100 - percent) + 50) / 100);
    }
}
