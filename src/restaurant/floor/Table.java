package restaurant.floor;

import restaurant.customer.Customer;

/**
 * A table. Stays occupied until its customer pays or leaves.
 *
 * Owner: Part 3
 */
public class Table {

    private final int id;
    private Customer occupant;

    public Table(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Customer getOccupant() {
        return occupant;
    }

    public boolean isFree() {
        return occupant == null;
    }

    /** Put a customer at this table. Throws IllegalStateException if already occupied. */
    public void assign(Customer customer) {
        if (customer==null){
            throw new IllegalArgumentException("Customer must not be null");
        }
        if (!isFree()){
            throw new IllegalStateException("Table " + id + " is already occupied by Customer #" + occupant.getId());
        }
            occupant = customer;
    }

    /** Free the table (customer paid or left). */
    public void release() {
        occupant = null;
    }

    @Override
    public String toString() {
        return "Table " + id + (isFree() ? " (free)" : " - Customer #" + occupant.getId());
    }
}
