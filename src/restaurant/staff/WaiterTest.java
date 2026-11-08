package restaurant.staff;

import restaurant.customer.CriticCustomer;
import restaurant.customer.CustomerState;
import restaurant.menu.Menu;
import restaurant.menu.MenuItem;
import restaurant.menu.Order;

/** Checks that Waiter.serve() triggers the Critic's reaction. Run: java -cp out restaurant.staff.WaiterTest */
public class WaiterTest {

    public static void main(String[] args) {
        CriticCustomer critic = new CriticCustomer(1, 1);
        MenuItem pasta = Menu.standardMenu().getItem(2);
        critic.seatAt(null);
        Order order = new Order(critic, pasta, pasta.getPriceCents(), 2);   // ordered turn 2
        critic.placeOrder(order);
        order.advancePreparation();
        order.advancePreparation();                                          // now READY

        critic.applyWaitingPenalty();                                        // 100 -> 88
        new Waiter().serve(order, 4);                                        // served turn 4: fast

        System.out.println("Critic is EATING: " + (critic.getState() == CustomerState.EATING));
        System.out.println("Satisfaction 88 -> 98: " + (critic.getSatisfaction() == 98));
    }
}