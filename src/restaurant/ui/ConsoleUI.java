package restaurant.ui;

import java.util.List;
import java.util.Scanner;

import restaurant.game.GameConfig;
import restaurant.game.Restaurant;

/**
 * Command-line boundary. ONLY displays state and collects the manager's choices.
 * No game rules in here (brief 2.2).
 *
 * Owner: Part 4
 */
public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);

    public void showTurnHeader(int turn, int maxTurns) {
        System.out.println();
        System.out.println("=== Turn " + turn + " / " + maxTurns + " ===");
    }

    /**
     * Brief 3.1: revenue, average satisfaction of paid customers (N/A if none),
     * waiting queue, occupied tables, each customer's satisfaction, order status,
     * and the staff tasks available.
     */
    public void showTurnStart(int turn, Restaurant restaurant) {
        // TODO(Part 4): full display
    }

    /**
     * Prints the options numbered 1..n and returns the chosen index (0-based).
     * Must handle non-numbers and out-of-range input by asking again, never crashing.
     */
    public int askChoice(String question, List<String> options) {
        // TODO(Part 4): use scanner, validate input
        throw new UnsupportedOperationException("TODO Part 4: ConsoleUI.askChoice");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    /** Invalid choice: explain why it failed (brief 3.4). */
    public void showError(String reason) {
        System.out.println("Not allowed: " + reason);
    }

    /**
     * Brief 3.5: served count, unhappy departures, revenue, average satisfaction
     * of paid customers (N/A if none), turns used.
     */
    public void showTurnEnd(int turn, Restaurant restaurant) {
        // TODO(Part 4): full summary
    }

    /** Victory or Defeat, with each target and whether it was met. */
    public void showFinalResult(boolean victory, Restaurant restaurant, GameConfig config) {
        // TODO(Part 4): show all three targets vs actual values
        System.out.println();
        System.out.println(victory ? "VICTORY" : "DEFEAT");
    }
}
