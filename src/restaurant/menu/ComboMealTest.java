package restaurant.menu;

public class ComboMealTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        MenuItem salad = new MenuItem("Salad", Money.dollars(9), 1);
        MenuItem burger = new MenuItem("Burger", Money.dollars(15), 1);
        MenuItem pasta = new MenuItem("Pasta", Money.dollars(18), 2);

        ComboMeal saladPasta = new ComboMeal(salad, pasta);
        ComboMeal burgerPasta = new ComboMeal(burger, pasta);
        ComboMeal saladBurger = new ComboMeal(salad, burger);

        check(saladPasta.getPriceCents() == 2430, "Salad + Pasta costs $24.30");
        check(burgerPasta.getPriceCents() == 2970, "Burger + Pasta costs $29.70");
        check(saladBurger.getPriceCents() == 2160, "Salad + Burger costs $21.60");

        check(saladPasta.getPrepUnits() == 3, "Salad + Pasta needs 3 prep units");
        check(burgerPasta.getPrepUnits() == 3, "Burger + Pasta needs 3 prep units");
        check(saladBurger.getPrepUnits() == 2, "Salad + Burger needs 2 prep units");

        check(saladPasta.getName().equals("Salad + Pasta combo"), "Combo name is correct");
        check(saladPasta.getFirst() == salad, "First component is Salad");
        check(saladPasta.getSecond() == pasta, "Second component is Pasta");

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
