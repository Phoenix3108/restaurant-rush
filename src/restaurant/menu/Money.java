package restaurant.menu;

/**
 * Money helpers. All amounts are stored as integer cents
 * and only converted to dollars for display.
 */
public final class Money {

    private Money() {
    }

    public static int dollars(int wholeDollars) {
        return wholeDollars * 100;
    }

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
