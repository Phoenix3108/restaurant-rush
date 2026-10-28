package restaurant.staff;

import java.util.List;

import restaurant.customer.Customer;

/**
 * Automatically handles at most ONE payment per turn (no manager choice).
 *
 * Owner: Part 3
 */
public class Cashier extends Staff {

    public Cashier() {
        super("Cashier");
    }

    /**
     * Pick the oldest customer for whom customer.canPay(currentTurn) is true
     * (earliest readyToPayTurn; tie -> lower id), then:
     *   customer.getOrder().markPaid();
     *   customer.markPaid();
     *   customer.getTable().release();
     * Revenue is recorded by the controller via Restaurant.recordPayment(customer).
     *
     * @return the customer who paid, or null if nobody could pay this turn
     */
    public Customer collectPayment(List<Customer> customers, int currentTurn) {
        if (hasActedThisTurn()){
            return null;
        }
        Customer oldest = null;
        for (Customer c : customers){
            if (!c.canPay(currentTurn)){
                continue;
            }
            if (oldest==null||c.getReadyToPayTurn()<oldest.getReadyToPayTurn()){
                oldest = c;
            }
            else if (c.getReadyToPayTurn()==oldest.getReadyToPayTurn()&&c.getId()<oldest.getId()){
                oldest = c;
            }
        }
        if (oldest==null){
            return null;
        }
        oldest.getOrder().markPaid();
        oldest.markPaid();
        oldest.getTable().release();
        markActed();
        return oldest;
    }
}
