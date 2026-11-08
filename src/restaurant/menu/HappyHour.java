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

    public boolean activate(int turn) {
        if (used) {
            return false;
        }

        used = true;
        activationTurn = turn;
        return true;
    }

    public boolean hasBeenUsed() {
        return used;
    }

    public boolean isActive(int turn) {
        return used && turn >= activationTurn
                && turn <= activationTurn + 1;
    }

    public int remainingTurns(int turn) {
        if (!isActive(turn)) {
            return 0;
        }

        return activationTurn + 2 - turn;
    }

    public int apply(int cents, int turn) {
        if (isActive(turn)) {
            return Money.percentOff(cents, 20);
        }

        return cents;
    }
}
