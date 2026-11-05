package restaurant.game;

import restaurant.customer.Customer;

/**
 * Creates one kind of customer. GameConfig pairs each arrival turn with a factory,
 * so the controller never needs an if-else on customer type: adding a new type
 * (e.g. VIP or Critic) only means passing a different factory in the arrival list.
 *
 * Owner: Part 4
 */
@FunctionalInterface
public interface CustomerFactory {

    /** Regular customers (the Stage 1 base class). */
    CustomerFactory REGULAR = Customer::new;

    Customer create(int id, int arrivalTurn);
}
