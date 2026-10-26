package restaurant.menu;

public class MenuTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        Menu menu = Menu.standardMenu();

        // Test menu size
        check(menu.size() == 3, "Menu contains three items");

        // Test item names
        check(menu.getItem(0).getName().equals("Salad"),
                "First item is Salad");

        check(menu.getItem(1).getName().equals("Burger"),
                "Second item is Burger");

        check(menu.getItem(2).getName().equals("Pasta"),
                "Third item is Pasta");

        // Test prices in cents
        check(menu.getItem(0).getPriceCents() == 900,
                "Salad costs $9.00");

        check(menu.getItem(1).getPriceCents() == 1500,
                "Burger costs $15.00");

        check(menu.getItem(2).getPriceCents() == 1800,
                "Pasta costs $18.00");

        // Test preparation units
        check(menu.getItem(0).getPrepUnits() == 1,
                "Salad needs one prep unit");

        check(menu.getItem(1).getPrepUnits() == 1,
                "Burger needs one prep unit");

        check(menu.getItem(2).getPrepUnits() == 2,
                "Pasta needs two prep units");

        // Print summary
        System.out.println("\n========================");
        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);
        System.out.println("========================");

        if (failed > 0) {
            throw new AssertionError("Some menu tests failed.");
        }
    }

    private static void check(boolean condition, String description) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + description);
        } else {
            failed++;
            System.out.println("FAIL: " + description);
        }
    }
}