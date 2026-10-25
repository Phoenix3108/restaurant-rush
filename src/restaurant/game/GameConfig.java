package restaurant.game;

import java.util.Arrays;

import restaurant.menu.Money;

/**
 * Game settings. baseSetting() is the required reproducible setting from brief 2.1.
 *
 * Owner: Part 4
 */
public class GameConfig {

    private final int maxTurns;
    private final int numTables;
    private final int[] arrivalTurns;
    private final int targetPaidCustomers;
    private final int targetRevenueCents;
    private final int targetAverageSatisfaction;

    public GameConfig(int maxTurns, int numTables, int[] arrivalTurns,
                      int targetPaidCustomers, int targetRevenueCents, int targetAverageSatisfaction) {
        this.maxTurns = maxTurns;
        this.numTables = numTables;
        this.arrivalTurns = arrivalTurns.clone();
        this.targetPaidCustomers = targetPaidCustomers;
        this.targetRevenueCents = targetRevenueCents;
        this.targetAverageSatisfaction = targetAverageSatisfaction;
    }

    /** 18 turns, 2 tables, arrivals on 1/4/7/10/13/16; win = 4 paid, $45, avg satisfaction 60. */
    public static GameConfig baseSetting() {
        return new GameConfig(18, 2, new int[] {1, 4, 7, 10, 13, 16}, 4, Money.dollars(45), 60);
    }

    public int getMaxTurns() {
        return maxTurns;
    }

    public int getNumTables() {
        return numTables;
    }

    public boolean isArrivalTurn(int turn) {
        return Arrays.stream(arrivalTurns).anyMatch(t -> t == turn);
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
}
