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

    public Menu(List<MenuItem> items) {
        this.items = List.copyOf(items);
    }

    /** The base-setting menu from brief 2.1. */
    public static Menu standardMenu() {
        return new Menu(List.of(
                new MenuItem("Salad", Money.dollars(9), 1),
                new MenuItem("Burger", Money.dollars(15), 1),
                new MenuItem("Pasta", Money.dollars(18), 2)));
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public MenuItem getItem(int index) {
        return items.get(index);
    }

    public int size() {
        return items.size();
    }

    /**
     * Returns the most expensive item on the menu.
     */
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
