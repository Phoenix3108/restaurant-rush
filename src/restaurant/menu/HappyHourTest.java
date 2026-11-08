package restaurant.menu;

public class HappyHourTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        HappyHour happyHour = new HappyHour();

        check(!happyHour.hasBeenUsed(), "Happy Hour has not been used initially");
        check(!happyHour.isActive(4), "Happy Hour is inactive before activation");

        check(happyHour.activate(5), "Happy Hour activates successfully");
        check(happyHour.hasBeenUsed(), "Happy Hour is marked as used");

        check(happyHour.isActive(5), "Happy Hour is active on activation turn");
        check(happyHour.isActive(6), "Happy Hour is active on next turn");
        check(!happyHour.isActive(7), "Happy Hour is inactive after two turns");

        check(happyHour.remainingTurns(5) == 2, "Two turns remain on activation turn");
        check(happyHour.remainingTurns(6) == 1, "One turn remains on next turn");
        check(happyHour.remainingTurns(7) == 0, "No turns remain after expiry");

        check(!happyHour.activate(8), "Happy Hour cannot be activated a second time");

        check(happyHour.apply(1500, 5) == 1200, "20% discount applies on turn 5");
        check(happyHour.apply(1350, 6) == 1080, "20% discount applies on turn 6");
        check(happyHour.apply(1500, 7) == 1500, "No discount applies after expiry");

        System.out.println("\nTests passed: " + passed);
        System.out.println("Tests failed: " + failed);
    }

    private static void check(boolean condition, String message) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + message);
        } else {
            failed++;
            System.out.println("FAIL: " + message);
        }
    }
}
