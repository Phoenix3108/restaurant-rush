package restaurant.game;

import java.util.List;
import java.util.Optional;

import restaurant.menu.Money;

/**
 * Game settings. baseSetting() is the required reproducible setting from brief 2.1.
 *
 * Owner: Part 4
 */
public class GameConfig {

    private final int maxTurns;
    private final int numTables;
    private final List<Arrival> arrivals;
    private final int targetPaidCustomers;
    private final int targetRevenueCents;
    private final int targetAverageSatisfaction;

    public GameConfig(int maxTurns, int numTables, List<Arrival> arrivals,
                      int targetPaidCustomers, int targetRevenueCents, int targetAverageSatisfaction) {
        this.maxTurns = maxTurns;
        this.numTables = numTables;
        this.arrivals = List.copyOf(arrivals);
        this.targetPaidCustomers = targetPaidCustomers;
        this.targetRevenueCents = targetRevenueCents;
        this.targetAverageSatisfaction = targetAverageSatisfaction;
    }

    /** 18 turns, 2 tables, arrivals on 1/4/7/10/13/16; win = 4 paid, $45, avg satisfaction 60. */
    public static GameConfig baseSetting() {
        return new GameConfig(18, 2,
                List.of(Arrival.regular(1), Arrival.regular(4), Arrival.regular(7),
                        Arrival.regular(10), Arrival.regular(13), Arrival.regular(16)),
                4, Money.dollars(45), 60);
    }

    public int getMaxTurns() {
        return maxTurns;
    }

    public int getNumTables() {
        return numTables;
    }

    public List<Arrival> getArrivals() {
        return arrivals;
    }

    /** The arrival scheduled for this turn, if any. */
    public Optional<Arrival> getArrivalAt(int turn) {
        return arrivals.stream().filter(a -> a.turn() == turn).findFirst();
    }

    public int getTargetPaidCustomers() {
        return targetPaidCustomers;
    }

    public int getTargetRevenueCents() {
        return targetRevenueCents;
    }

    public int getTargetAverageSatisfaction() {
        return targetAverageSatisfaction;
    }

    // ---------- step 6: each target on its own, so the UI can show which were met ----------

    public boolean isPaidTargetMet(Restaurant restaurant) {
        return restaurant.getPaidCount() >= targetPaidCustomers;
    }

    public boolean isRevenueTargetMet(Restaurant restaurant) {
        return restaurant.getRevenueCents() >= targetRevenueCents;
    }

    /** Needs at least one paid customer (N/A counts as not met). */
    public boolean isSatisfactionTargetMet(Restaurant restaurant) {
        return restaurant.hasPaidCustomers()
                && restaurant.getAveragePaidSatisfaction() >= targetAverageSatisfaction;
    }
}
