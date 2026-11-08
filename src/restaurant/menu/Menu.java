package restaurant.menu;

import java.util.Collections;
import java.util.List;

/**
 * The list of dishes customers can choose from.
 *
 * Owner: Part 1
 */
public class Menu {

    private final List<MenuItem> items;
    private final List<ComboMeal> combos;

    public Menu(List<MenuItem> items) {
        this(items, List.of());
    }

    public Menu(List<MenuItem> items, List<ComboMeal> combos) {
        this.items = List.copyOf(items);
        this.combos = List.copyOf(combos);
    }

    public static Menu standardMenu() {
        MenuItem salad = new MenuItem("Salad", Money.dollars(9), 1);
        MenuItem burger = new MenuItem("Burger", Money.dollars(15), 1);
        MenuItem pasta = new MenuItem("Pasta", Money.dollars(18), 2);

        ComboMeal saladPasta = new ComboMeal(salad, pasta);

        return new Menu(
                List.of(salad, burger, pasta),
                List.of(saladPasta)
        );
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<ComboMeal> getCombos() {
        return Collections.unmodifiableList(combos);
    }

    public MenuItem getItem(int index) {
        return items.get(index);
    }

    public ComboMeal getCombo(int index) {
        return combos.get(index);
    }

    public int size() {
        return items.size();
    }

    public MenuItem getMostExpensive() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Menu is empty.");
        }

        MenuItem mostExpensive = items.get(0);

        for (MenuItem item : items) {
            if (item.getPriceCents() > mostExpensive.getPriceCents()) {
                mostExpensive = item;
            }
        }

        return mostExpensive;
    }
}
