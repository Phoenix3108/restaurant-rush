package restaurant.customer;

/**
 * The standard customer: loses 8 satisfaction per waiting turn and pays full price.
 * Everything else is the shared behaviour in Customer.
 *
 * Owner: Part 2
 */
public class RegularCustomer extends Customer {

    private static final int LOSS_PER_TURN = 8;

    public RegularCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    public String getTypeLabel() {
        return "Regular";
    }

    @Override
    public int getSatisfactionLossPerTurn() {
        return LOSS_PER_TURN;
    }
}
