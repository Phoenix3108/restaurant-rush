package restaurant.staff;

import restaurant.customer.CriticCustomer;
import restaurant.customer.Customer;
import restaurant.customer.CustomerState;
import restaurant.customer.RegularCustomer;
import restaurant.customer.VIPCustomer;
import restaurant.floor.Table;
import restaurant.menu.HappyHour;
import restaurant.menu.Menu;
import restaurant.menu.MenuItem;
import restaurant.menu.Order;

/** Checks for the Waiter: Critic reaction and Happy Hour prices. Run: java -cp out restaurant.staff.WaiterTest */
public class WaiterTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testServeTriggersCriticReaction();
        testHappyHourPrices();

        System.out.println("\nTests passed: " + passed + ", failed: " + failed);
        if (failed > 0) {
            throw new AssertionError("Some tests failed.");
        }
    }

    private static void check(boolean condition, String name) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    private static void testServeTriggersCriticReaction() {
        CriticCustomer critic = new CriticCustomer(1, 1);
        MenuItem pasta = Menu.standardMenu().getItem(2);
        critic.seatAt(null);
        Order order = new Order(critic, pasta, pasta.getPriceCents(), 2);   // ordered turn 2
        critic.placeOrder(order);
        order.advancePreparation();
        order.advancePreparation();                                          // now READY

        critic.applyWaitingPenalty();                                        // 100 -> 88
        new Waiter().serve(order, 4);                                        // served turn 4: fast

        check(critic.getState() == CustomerState.EATING, "Critic is EATING after Waiter.serve");
        check(critic.getSatisfaction() == 98, "Waiter.serve triggers the Critic's +10: 88 -> 98");
    }

    /** Seats the customer and has a fresh Waiter take their order; returns the locked bill. */
    private static int billFor(Customer customer, HappyHour happyHour, int turn) {
        customer.seatAt(new Table(1));
        new Waiter().takeOrder(customer, Menu.standardMenu(), turn, happyHour);
        return customer.getOrder().getBillCents();
    }

    private static void testHappyHourPrices() {
        HappyHour happyHour = new HappyHour();
        happyHour.activate(5);                                               // active on turns 5 and 6

        // Customer #2 orders Burger, #3 orders the Salad + Pasta combo (food choice rule)
        check(billFor(new RegularCustomer(2, 1), happyHour, 5) == 1200, "Happy Hour: Regular Burger $12.00");
        check(billFor(new VIPCustomer(2, 1), happyHour, 6) == 1080, "Happy Hour: VIP Burger $10.80 (brief example)");
        check(billFor(new RegularCustomer(3, 1), happyHour, 5) == 1944, "Happy Hour: Salad + Pasta combo $19.44");
        check(billFor(new RegularCustomer(2, 1), happyHour, 7) == 1500, "after Happy Hour: Burger $15.00");

        Customer early = new RegularCustomer(2, 1);
        early.seatAt(new Table(1));
        ActionResult result = new Waiter().takeOrder(early, Menu.standardMenu(), 5, happyHour);
        check(result.getMessage().equals("Customer #2 orders Burger ($12.00, Happy Hour)"),
                "message shows the Happy Hour price");
        check(!happyHour.isActive(7) && early.getOrder().getBillCents() == 1200,
                "order taken in Happy Hour keeps $12.00 after it ends");

        Customer oldCall = new RegularCustomer(2, 1);
        oldCall.seatAt(new Table(1));
        new Waiter().takeOrder(oldCall, Menu.standardMenu(), 5);
        check(oldCall.getOrder().getBillCents() == 1500, "old 3-argument takeOrder never applies Happy Hour");
    }
}