package restaurant.customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Customers waiting for a table, in arrival order. The manager may seat any
 * of them (not only the first), so this is a list rather than a strict FIFO.
 *
 * Owner: Part 2
 */
public class WaitingQueue {

    private final List<Customer> customers = new ArrayList<>();

    public void add(Customer customer) {
        customers.add(customer);
    }

    public boolean remove(Customer customer) {
        return customers.remove(customer);
    }

    public boolean contains(Customer customer) {
        return customers.contains(customer);
    }

    /** Read-only view, oldest first. */
    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

    public boolean isEmpty() {
        return customers.isEmpty();
    }

    public int size() {
        return customers.size();
    }
}
