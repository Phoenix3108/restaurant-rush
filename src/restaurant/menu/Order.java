package restaurant.menu;

import restaurant.customer.Customer;

/**
 * One customer's order. The bill is fixed at the moment the order is taken
 * (this matters later for VIP and Happy Hour pricing).
 *
 * Owner: Part 1
 */
public class Order {

    private static int nextId = 1;

    private final int id;
    private final Customer customer;
    private final MenuItem item;
    private final int billCents;
    private final int turnTaken;
    private int unitsDone;
    private OrderStatus status;

    public Order(Customer customer, MenuItem item, int billCents, int turnTaken) {
        this.id = nextId++;
        this.customer = customer;
        this.item = item;
        this.billCents = billCents;
        this.turnTaken = turnTaken;
        this.unitsDone = 0;
        this.status = OrderStatus.IN_PREPARATION;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public MenuItem getItem() {
        return item;
    }

    public int getBillCents() {
        return billCents;
    }

    public int getTurnTaken() {
        return turnTaken;
    }

    public int getUnitsDone() {
        return unitsDone;
    }

    public int getUnitsRemaining() {
        return item.getPrepUnits() - unitsDone;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public boolean isReady() {
        return status == OrderStatus.READY;
    }

    /**
     * Chef does one preparation unit. When the last unit is done the order becomes READY.
     *
     * @return true if the order became READY with this unit
     * @throws IllegalStateException if the order is not IN_PREPARATION
     */
    public boolean advancePreparation() {
        // TODO(Part 1): unitsDone++, switch to READY when no units remain
        throw new UnsupportedOperationException("TODO Part 1: Order.advancePreparation");
    }

    /** READY -> SERVED. Throws IllegalStateException otherwise. */
    public void markServed() {
        // TODO(Part 1)
        throw new UnsupportedOperationException("TODO Part 1: Order.markServed");
    }

    /** SERVED -> PAID. Throws IllegalStateException otherwise. */
    public void markPaid() {
        // TODO(Part 1)
        throw new UnsupportedOperationException("TODO Part 1: Order.markPaid");
    }

    /** IN_PREPARATION or READY -> CANCELLED (customer left). */
    public void cancel() {
        // TODO(Part 1)
        throw new UnsupportedOperationException("TODO Part 1: Order.cancel");
    }

    @Override
    public String toString() {
        return "Order #" + id + " " + item.getName() + " for Customer #" + customer.getId()
                + " [" + status + ", " + unitsDone + "/" + item.getPrepUnits() + " units]";
    }
}
