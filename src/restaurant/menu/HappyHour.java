package restaurant.menu;

/**
 * Happy Hour gives a 20% discount for two turns.
 * It can only be activated once per game.
 *
 * Owner: Part 1
 */
public class HappyHour {

    private boolean used = false;
    private int activationTurn = -1;

    /** Activates Happy Hour if it has not been used before. */
    public boolean activate(int turn) {
        if (used) {
            return false;
        }

        used = true;
        activationTurn = turn;
        return true;
    }

    /** Returns whether Happy Hour has already been used. */
    public boolean hasBeenUsed() {
        return used;
    }

    /** Returns whether Happy Hour is active on the given turn. */
    public boolean isActive(int turn) {
        return used && turn >= activationTurn
                && turn <= activationTurn + 1;
    }

    /** Returns the number of active turns remaining. */
    public int remainingTurns(int turn) {
        if (!isActive(turn)) {
            return 0;
        }

        return activationTurn + 2 - turn;
    }

    /** Applies the Happy Hour discount when it is active. */
    public int apply(int cents, int turn) {
        if (isActive(turn)) {
            return Money.percentOff(cents, 20);
        }

        return cents;
    }
}
