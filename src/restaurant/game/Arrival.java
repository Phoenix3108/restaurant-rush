package restaurant.game;

/**
 * One scheduled arrival: on {@code turn}, a customer made by {@code factory} joins the queue.
 *
 * Owner: Part 4
 */
public record Arrival(int turn, CustomerFactory factory) {

    public static Arrival regular(int turn) {
        return new Arrival(turn, CustomerFactory.REGULAR);
    }
}
