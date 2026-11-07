package restaurant.menu;

public class MoneyTest {

    public static void main(String[] args) {
        check(Money.dollars(9) == 900, "9 dollars converts to 900 cents");
        check(Money.dollars(15) == 1500, "15 dollars converts to 1500 cents");

        check(Money.format(900).equals("$9.00"), "900 cents formats as $9.00");
        check(Money.format(1500).equals("$15.00"), "1500 cents formats as $15.00");
        check(Money.format(2430).equals("$24.30"), "2430 cents formats as $24.30");
        check(Money.format(5).equals("$0.05"), "5 cents formats as $0.05");

        check(Money.percentOff(1500, 10) == 1350, "10% discount on $15");
        check(Money.percentOff(1800, 10) == 1620, "10% discount on $18");
        check(Money.percentOff(1500, 20) == 1200, "20% discount on $15");
        check(Money.percentOff(100, 33) == 67, "Discount rounds to nearest cent");

        System.out.println("\nAll Money tests completed!");
    }

    private static void check(boolean condition, String description) {
        if (!condition) {
            throw new AssertionError("FAIL: " + description);
        }
        System.out.println("PASS: " + description);
    }
}
