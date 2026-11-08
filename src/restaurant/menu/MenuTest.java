package restaurant.menu;

import java.util.List;

public class MenuTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        Menu menu = Menu.standardMenu();

        check(menu.size() == 3, "Menu contains three items");

        check(menu.getItem(0).getName().equals("Salad"),
                "First item is Salad");

        check(menu.getItem(1).getName().equals("Burger"),
                "Second item is Burger");

        check(menu.getItem(2).getName().equals("Pasta"),
                "Third item is Pasta");

        check(menu.getItem(0).getPriceCents() == 900,
                "Salad costs $9.00");

        check(menu.getItem(1).getPriceCents() == 1500,
                "Burger costs $15.00");

        check(menu.getItem(2).getPriceCents() == 1800,
                "Pasta costs $18.00");

        check(menu.getItem(0).getPrepUnits() == 1,
                "Salad needs one prep unit");

        check(menu.getItem(1).getPrepUnits() == 1,
                "Burger needs one prep unit");

        check(menu.getItem(2).getPrepUnits() == 2,
                "Pasta needs two prep units");

        // Stage 2 tests
        check(menu.getMostExpensive().getName().equals("Pasta"),
                "Pasta is the most expensive item");

        check(menu.getMostExpensive().getPriceCents() == 1800,
                "Most expensive item costs $18");

        Menu emptyMenu = new Menu(List.of());
        boolean exceptionThrown = false;

        try {
            emptyMenu.getMostExpensive();
        } catch (IllegalStateException e) {
            exceptionThrown = true;
        }

        check(exceptionThrown,
                "Empty menu throws IllegalStateException");

        // Stage 3 tests
        check(menu.getItems().size() == 3,
                "Combos are not included in regular menu items");

        check(menu.getCombos().size() == 1,
                "Menu contains one combo");

        check(menu.getCombo(0).getName().equals("Salad + Pasta combo"),
                "First combo is Salad + Pasta");

        check(menu.getCombo(0).getPriceCents() == 2430,
                "Salad + Pasta combo costs $24.30");

        check(menu.getCombo(0).getPrepUnits() == 3,
                "Salad + Pasta combo needs three prep units");

        check(menu.getMostExpensive().getName().equals("Pasta"),
                "Pasta remains the most expensive regular menu item");

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
