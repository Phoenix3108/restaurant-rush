package restaurant.menu;

/**
 * A combo meal containing two menu items with a 10% discount.
 *
 * Owner: Part 1
 */
public class ComboMeal extends MenuItem {

    private final MenuItem first;
    private final MenuItem second;

    public ComboMeal(MenuItem first, MenuItem second) {
        super(
            first.getName() + " + " + second.getName() + " combo",
            Money.percentOff(
                first.getPriceCents() + second.getPriceCents(), 10),
            first.getPrepUnits() + second.getPrepUnits()
        );

        this.first = first;
        this.second = second;
    }

    public MenuItem getFirst() {
        return first;
    }

    public MenuItem getSecond() {
        return second;
    }
}
