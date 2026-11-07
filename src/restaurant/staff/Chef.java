package restaurant.staff;

import restaurant.menu.Order;
import restaurant.menu.OrderStatus;

/**
 * Advances at most ONE order by ONE preparation unit per turn.
 * May work on an order the Waiter took earlier this same turn.
 *
 * Owner: Part 3
 */
public class Chef extends Staff {

    public Chef() {
        super("Chef");
    }

    /**
     * Fail if: already acted, order not IN_PREPARATION.
     * On success: order.advancePreparation(); message says if it is now READY.
     */
    public ActionResult prepare(Order order) {
        if (hasActedThisTurn()){
            return ActionResult.fail("Chef has already acted this turn");
        }
        if (order==null){
            return ActionResult.fail("Must choose an order");
        }  
        if (order.getStatus()!=OrderStatus.IN_PREPARATION){
            return ActionResult.fail("Order #" + order.getId() + " (" + order.getItem().getName() + " for Customer #" + order.getCustomer().getId() +  ") is " + order.getStatus() + ", not IN_PREPARATION");
        }
        boolean nowReady = order.advancePreparation();
        markActed();
        if (nowReady){
            return ActionResult.ok("Order #" + order.getId() + " (" + order.getItem().getName() + " for Customer #" + order.getCustomer().getId() +  ") is now READY");
        }
        else {
            return ActionResult.ok("Order #" + order.getId() + " (" + order.getItem().getName() + " for Customer #" + order.getCustomer().getId() +  ") is now " + order.getUnitsDone() + "/" + order.getItem().getPrepUnits() + " units done" );
        }
    }
}
