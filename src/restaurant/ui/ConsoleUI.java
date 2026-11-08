package restaurant.ui;

import java.util.List;
import java.util.Scanner;

import restaurant.customer.Customer;
import restaurant.floor.Table;
import restaurant.game.GameConfig;
import restaurant.game.Restaurant;
import restaurant.menu.Money;
import restaurant.menu.Order;

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
        System.out.println("Revenue: " + Money.format(restaurant.getRevenueCents())
                + " | Avg satisfaction (paid): " + averageText(restaurant));

        System.out.print("Waiting queue: ");
        List<Customer> queue = restaurant.getQueue().getCustomers();
        if (queue.isEmpty()) {
            System.out.println("(empty)");
        } else {
            StringBuilder line = new StringBuilder();
            for (Customer customer : queue) {
                if (line.length() > 0) {
                    line.append(", ");
                }
                line.append("#").append(customer.getId());
            }
            System.out.println(line);
        }

        System.out.println("Tables:");
        for (Table table : restaurant.getTables()) {
            System.out.println("  " + table);
        }

        System.out.println("Customers:");
        List<Customer> active = restaurant.getActiveCustomers();
        if (active.isEmpty()) {
            System.out.println("  (none in the restaurant)");
        }
        for (Customer customer : active) {
            Order order = customer.getOrder();
            System.out.println("  #" + customer.getId()
                    + "  " + customer.getTypeLabel()
                    + "  " + customer.getState()
                    + "  satisfaction " + customer.getSatisfaction()
                    + "  order: " + (order == null ? "none"
                            : order.getItem().getName() + " " + order.getStatus()
                                    + " (" + order.getUnitsDone() + "/" + order.getItem().getPrepUnits()
                                    + " units, bill " + Money.format(order.getBillCents()) + ")"));
        }

        System.out.println("Tasks available:");
        System.out.println("  Waiter: " + restaurant.getQueue().size() + " to seat ("
                + restaurant.getFreeTables().size() + " free table(s)), "
                + restaurant.getCustomersAwaitingOrder().size() + " order(s) to take, "
                + restaurant.getReadyOrders().size() + " order(s) to serve");
        System.out.println("  Chef:   " + restaurant.getOrdersInKitchen().size() + " order(s) in the kitchen");
        System.out.println("  Cashier (automatic): " + restaurant.getCustomersAbleToPay(turn).size()
                + " customer(s) ready to pay");
    }

    /**
     * Prints the options numbered 1..n and returns the chosen index (0-based).
     * Must handle non-numbers and out-of-range input by asking again, never crashing.
     */
    public int askChoice(String question, List<String> options) {
        System.out.println(question);
        for (int i = 0; i < options.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + options.get(i));
        }
        while (true) {
            System.out.print("Enter 1-" + options.size() + ": ");
            System.out.flush();
            if (!scanner.hasNextLine()) {
                throw new IllegalStateException("Input ended before the game finished.");
            }
            String input = scanner.nextLine().trim();
            if (Boolean.getBoolean("replay")) {
                // Test-run replay (java -Dreplay=true ...): input comes from a file, so echo it
                // to make the transcript show each choice.
                System.out.println(input);
            }
            try {
                int number = Integer.parseInt(input);
                if (number >= 1 && number <= options.size()) {
                    return number - 1;
                }
                System.out.println("Please enter a number from 1 to " + options.size() + ".");
            } catch (NumberFormatException e) {
                System.out.println("'" + input + "' is not a number. Please enter 1 to " + options.size() + ".");
            }
        }
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
        System.out.println("--- End of turn " + turn + " ---");
        System.out.println("Served: " + restaurant.getServedCount()
                + " | Paid: " + restaurant.getPaidCount()
                + " | Unhappy departures: " + restaurant.getUnhappyDepartures()
                + " | Revenue: " + Money.format(restaurant.getRevenueCents())
                + " | Avg satisfaction (paid): " + averageText(restaurant)
                + " | Turns used: " + turn);
    }

    /** Victory or Defeat, with each target and whether it was met. */
    public void showFinalResult(boolean victory, Restaurant restaurant, GameConfig config) {
        System.out.println();
        System.out.println("=== Final result ===");
        System.out.println(targetLine("Paid customers", restaurant.getPaidCount() + "",
                "at least " + config.getTargetPaidCustomers(), config.isPaidTargetMet(restaurant)));
        System.out.println(targetLine("Revenue", Money.format(restaurant.getRevenueCents()),
                "at least " + Money.format(config.getTargetRevenueCents()), config.isRevenueTargetMet(restaurant)));
        System.out.println(targetLine("Avg satisfaction (paid)", averageText(restaurant),
                "at least " + config.getTargetAverageSatisfaction(), config.isSatisfactionTargetMet(restaurant)));
        System.out.println("Unhappy departures: " + restaurant.getUnhappyDepartures());
        System.out.println(victory ? "VICTORY" : "DEFEAT");
    }

    private static String targetLine(String name, String actual, String target, boolean met) {
        return String.format("  %-24s %-8s (target %s) %s", name + ":", actual, target, met ? "MET" : "NOT MET");
    }

    private static String averageText(Restaurant restaurant) {
        return restaurant.hasPaidCustomers()
                ? String.format("%.1f", restaurant.getAveragePaidSatisfaction())
                : "N/A";
    }
}
