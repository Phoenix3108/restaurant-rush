package restaurant.staff;

import restaurant.customer.Customer;
import restaurant.customer.CustomerState;
import restaurant.customer.RegularCustomer;
import restaurant.customer.WaitingQueue;
import restaurant.floor.Table;

/** Checks for the Host's seating. Run: java -cp out restaurant.staff.HostTest */
public class HostTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        WaitingQueue queue = new WaitingQueue();
        Customer c1 = new RegularCustomer(1, 1);
        Customer c2 = new RegularCustomer(2, 1);
        Customer c3 = new RegularCustomer(3, 1);
        Customer notQueued = new RegularCustomer(4, 1);
        queue.add(c1);
        queue.add(c2);
        queue.add(c3);
        Table t1 = new Table(1);
        Table t2 = new Table(2);
        Table t3 = new Table(3);
        Host host = new Host();
        Waiter waiter = new Waiter();

        // Turn 1: refusals first, then a successful seat (refusals must not use up the task)
        check(!host.seat(notQueued, t1, queue).isSuccess(), "customer not in queue is refused");
        t3.assign(notQueued);
        check(!host.seat(c1, t3, queue).isSuccess(), "occupied table is refused");
        ActionResult ok = host.seat(c1, t1, queue);
        check(ok.isSuccess(), "Host can still seat after refusals (task not used up)");
        check(ok.getMessage().equals("Host seated Customer #1 at Table 1"), "success message");
        check(c1.getState() == CustomerState.SEATED && t1.getOccupant() == c1 && !queue.contains(c1),
                "customer is SEATED, at the table and out of the queue");
        check(!host.seat(c2, t2, queue).isSuccess(), "second Host seat in the same turn is refused");

        // Same turn: the Waiter can still seat (separate task)
        check(waiter.seat(c2, t2, queue).isSuccess(), "Host and Waiter can both seat in the same turn");

        // Next turn: the Host can act again
        host.startNewTurn();
        t3.release();
        check(host.seat(c3, t3, queue).isSuccess(), "Host can seat again next turn");

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
}