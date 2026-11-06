package restaurant.game;

import restaurant.customer.Customer;
import restaurant.customer.RegularCustomer;

/**
 * Creates one kind of customer. GameConfig pairs each arrival turn with a factory,
 * so the controller never needs an if-else on customer type: adding a new type
 * (e.g. VIP or Critic) only means passing a different factory in the arrival list.
 *
 * Owner: Part 4
 */
@FunctionalInterface
public interface CustomerFactory {

    /** Regular customers. (Customer is abstract from Stage 2, so this names the concrete type.) */
    CustomerFactory REGULAR = RegularCustomer::new;

    Customer create(int id, int arrivalTurn);
}
