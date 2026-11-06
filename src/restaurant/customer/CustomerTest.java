package restaurant.customer;

import java.util.ArrayList;
import java.util.List;

import restaurant.menu.Menu;
import restaurant.menu.MenuItem;
import restaurant.menu.Order;

/**
 * Checks for the customer types (run: java -cp out restaurant.customer.CustomerTest).
 * Same style as the menu tests: no libraries needed.
 *
 * Owner: Part 2
 */
public class CustomerTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testRegular();
        testVip();
        testPolymorphicPenalty();
        testVipLeavesAfterTwentyTurns();
        testOnServedHook();
        testAdjustSatisfactionLimits();
        testEatingTimingUnchanged();

        System.out.println("\n========================");
        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);
        System.out.println("========================");
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

    private static void testRegular() {
        Customer c = new RegularCustomer(1, 1);
        MenuItem burger = Menu.standardMenu().getItem(1);
        check(c.getTypeLabel().equals("Regular"), "Regular label");
        check(c.getSatisfactionLossPerTurn() == 8, "Regular loses 8 per turn");
        check(c.computeBillCents(burger) == 1500, "Regular pays full price for Burger ($15.00)");
    }

    private static void testVip() {
        Customer c = new VIPCustomer(2, 4);
        Menu menu = Menu.standardMenu();
        check(c.getTypeLabel().equals("VIP"), "VIP label");
        check(c.getSatisfactionLossPerTurn() == 5, "VIP loses 5 per turn");
        check(c.computeBillCents(menu.getItem(0)) == 810, "VIP Salad $9.00 -> $8.10");
        check(c.computeBillCents(menu.getItem(1)) == 1350, "VIP Burger $15.00 -> $13.50");
        check(c.computeBillCents(menu.getItem(2)) == 1620, "VIP Pasta $18.00 -> $16.20");
        check(c.toString().startsWith("VIP Customer #2"), "toString shows the type");
    }

    /** The same call on a Customer reference gives each type's own loss (no type checks). */
    private static void testPolymorphicPenalty() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new RegularCustomer(1, 1));
        customers.add(new VIPCustomer(2, 1));
        for (Customer c : customers) {
            c.applyWaitingPenalty();
        }
        check(customers.get(0).getSatisfaction() == 92, "Regular 100 -> 92 after one waiting turn");
        check(customers.get(1).getSatisfaction() == 95, "VIP 100 -> 95 after one waiting turn");
    }

    private static void testVipLeavesAfterTwentyTurns() {
        Customer c = new VIPCustomer(3, 1);
        int turns = 0;
        boolean mustLeave = false;
        while (!mustLeave) {
            mustLeave = c.applyWaitingPenalty();
            turns++;
        }
        check(turns == 20 && c.getSatisfaction() == 0, "VIP reaches 0 after 20 waiting turns (Regular: 13)");
    }

    /** A subclass that records onServed, to prove serve() calls the hook exactly once. */
    private static class RecordingCustomer extends RegularCustomer {
        int servedCalls = 0;
        int servedOnTurn = -1;

        RecordingCustomer(int id) {
            super(id, 1);
        }

        @Override
        protected void onServed(int currentTurn) {
            servedCalls++;
            servedOnTurn = currentTurn;
        }

        void change(int delta) {
            adjustSatisfaction(delta);
        }
    }

    private static void testOnServedHook() {
        RecordingCustomer c = new RecordingCustomer(4);
        Menu menu = Menu.standardMenu();
        c.seatAt(null);
        c.placeOrder(new Order(c, menu.getItem(0), 900, 2));
        check(c.servedCalls == 0, "onServed not called before serving");
        c.serve(3);
        check(c.servedCalls == 1 && c.servedOnTurn == 3, "serve(3) calls onServed(3) once");
    }

    private static void testAdjustSatisfactionLimits() {
        RecordingCustomer c = new RecordingCustomer(5);
        c.change(+10);
        check(c.getSatisfaction() == 100, "satisfaction never goes above 100");
        c.change(-150);
        check(c.getSatisfaction() == 0, "satisfaction never goes below 0");
    }

    private static void testEatingTimingUnchanged() {
        Customer c = new VIPCustomer(6, 1);
        Menu menu = Menu.standardMenu();
        c.seatAt(null);
        c.placeOrder(new Order(c, menu.getItem(1), c.computeBillCents(menu.getItem(1)), 2));
        c.serve(4);
        check(!c.finishEatingIfDone(4), "not done eating on the turn served");
        check(c.finishEatingIfDone(5) && !c.canPay(5), "READY_TO_PAY next turn, cannot pay same turn");
        check(c.canPay(6), "can pay the turn after");
        check(c.getOrder().getBillCents() == 1350, "VIP bill locked at order time ($13.50)");
    }
}
