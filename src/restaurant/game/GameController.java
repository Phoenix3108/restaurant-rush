package restaurant.game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import restaurant.customer.Customer;
import restaurant.floor.Table;
import restaurant.menu.Money;
import restaurant.menu.Order;
import restaurant.staff.ActionResult;
import restaurant.staff.Chef;
import restaurant.staff.Waiter;
import restaurant.ui.ConsoleUI;

/**
 * Runs the game: 18 turns, each with the six steps from brief 3.2, then the result.
 * All game rules live here and in the domain classes, never in ConsoleUI.
 *
 * Owner: Part 4
 */
public class GameController {

    private final GameConfig config;
    private final ConsoleUI ui;
    private final Restaurant restaurant;
    private int nextCustomerId = 1;

    public GameController(GameConfig config, ConsoleUI ui) {
        this.config = config;
        this.ui = ui;
        this.restaurant = new Restaurant(config);
    }

    /** Plays all turns (no early ending), then shows Victory or Defeat. */
    public void run() {
        for (int turn = 1; turn <= config.getMaxTurns(); turn++) {
            playTurn(turn);
        }
        ui.showFinalResult(isVictory(), restaurant, config);
    }

    private void playTurn(int turn) {
        restaurant.getWaiter().startNewTurn();
        restaurant.getChef().startNewTurn();
        restaurant.getCashier().startNewTurn();

        ui.showTurnHeader(turn, config.getMaxTurns());
        handleArrivals(turn);                // step 1
        ui.showTurnStart(turn, restaurant);   // shown after arrival so the manager sees the new customer
        handleWaiterChoice(turn);             // step 2 + 3: Waiter acts first...
        handleChefChoice(turn);               // ...then Chef
        handleEatingAndPayment(turn);         // step 4
        handleWaitingAndAbandonment(turn);    // step 5
        ui.showTurnEnd(turn, restaurant);
    }

    /**
     * Step 1: the scheduled customer joins the queue with satisfaction 100.
     * The arrival's factory decides the customer type, so there is no type check here.
     */
    private void handleArrivals(int turn) {
        config.getArrivalAt(turn).ifPresent(arrival -> {
            Customer customer = arrival.factory().create(nextCustomerId++, turn);
            restaurant.addArrival(customer);
            ui.showMessage(customer + " arrives and joins the queue.");
        });
    }

    /**
     * Step 2/3 (Waiter): build options (seat X at table Y / take order of X / serve order Z / Wait),
     * ask ui.askChoice(...), call the Waiter method. If the result failed, ui.showError(reason)
     * and ask again (the task is not used up).
     *
     * Every table is listed for seating, occupied ones too, so the manager can pick an
     * invalid seat and get the Waiter's explanation (brief 3.4).
     */
    private void handleWaiterChoice(int turn) {
        Waiter waiter = restaurant.getWaiter();
        List<String> labels = new ArrayList<>();
        List<Supplier<ActionResult>> actions = new ArrayList<>();

        for (Customer customer : restaurant.getQueue().getCustomers()) {
            for (Table table : restaurant.getTables()) {
                labels.add("Seat Customer #" + customer.getId() + " at Table " + table.getId()
                        + (table.isFree() ? "" : " (occupied)"));
                actions.add(() -> waiter.seat(customer, table, restaurant.getQueue()));
            }
        }
        for (Customer customer : restaurant.getCustomersAwaitingOrder()) {
            labels.add("Take order of Customer #" + customer.getId()
                    + " at Table " + customer.getTable().getId());
            actions.add(() -> waiter.takeOrder(customer, restaurant.getMenu(), turn));
        }
        for (Order order : restaurant.getReadyOrders()) {
            labels.add("Serve Order #" + order.getId() + " (" + order.getItem().getName()
                    + ") to Customer #" + order.getCustomer().getId());
            actions.add(() -> waiter.serve(order, turn));
        }

        runChoice("Waiter: choose a task", labels, actions);
    }

    /** Step 2/3 (Chef): options = each order in the kitchen, or Wait. Same retry loop as the Waiter. */
    private void handleChefChoice(int turn) {
        Chef chef = restaurant.getChef();
        List<String> labels = new ArrayList<>();
        List<Supplier<ActionResult>> actions = new ArrayList<>();

        for (Order order : restaurant.getOrdersInKitchen()) {
            labels.add("Prepare Order #" + order.getId() + " (" + order.getItem().getName()
                    + " for Customer #" + order.getCustomer().getId() + ", "
                    + order.getUnitsDone() + "/" + order.getItem().getPrepUnits() + " units done)");
            actions.add(() -> chef.prepare(order));
        }

        runChoice("Chef: choose a task", labels, actions);
    }

    /**
     * Adds "Wait" as the last option and asks until the manager picks Wait or an action
     * succeeds. A failed action shows the reason and asks again (the task is not used up).
     */
    private void runChoice(String question, List<String> labels, List<Supplier<ActionResult>> actions) {
        List<String> options = new ArrayList<>(labels);
        options.add("Wait");
        while (true) {
            int choice = ui.askChoice(question, options);
            if (choice == actions.size()) {
                ui.showMessage("Waits this turn.");
                return;
            }
            ActionResult result = actions.get(choice).get();
            if (result.isSuccess()) {
                ui.showMessage(result.getMessage());
                return;
            }
            ui.showError(result.getMessage());
        }
    }

    /**
     * Step 4: for each active customer, customer.finishEatingIfDone(turn);
     * then Customer paid = cashier.collectPayment(...); if paid != null, restaurant.recordPayment(paid).
     */
    private void handleEatingAndPayment(int turn) {
        for (Customer customer : restaurant.getActiveCustomers()) {
            if (customer.finishEatingIfDone(turn)) {
                ui.showMessage("Customer #" + customer.getId() + " finished eating and is ready to pay.");
            }
        }
        Customer paid = restaurant.getCashier().collectPayment(restaurant.getActiveCustomers(), turn);
        if (paid != null) {
            restaurant.recordPayment(paid);
            ui.showMessage("Cashier: Customer #" + paid.getId() + " paid "
                    + Money.format(paid.getOrder().getBillCents())
                    + " (satisfaction " + paid.getSatisfaction() + ").");
        }
    }

    /**
     * Step 5: every active customer applies the waiting penalty; anyone who hits 0 calls
     * customer.leave() (also remove from queue if waiting) and restaurant.recordDeparture(...).
     */
    private void handleWaitingAndAbandonment(int turn) {
        for (Customer customer : restaurant.getActiveCustomers()) {
            if (customer.applyWaitingPenalty()) {
                customer.leave();
                restaurant.recordDeparture(customer);   // also removes them from the queue
                ui.showMessage("Customer #" + customer.getId() + " ran out of patience and left.");
            }
        }
    }

    /** Step 6: all three targets from GameConfig must be met. */
    private boolean isVictory() {
        return config.isPaidTargetMet(restaurant)
                && config.isRevenueTargetMet(restaurant)
                && config.isSatisfactionTargetMet(restaurant);
    }
}
