# Reflection: Dylan (Part 3, staff and tables)

## What did I learn?

I learned to use the Open Closed Principle to design in a way that allows new behavior without modifying previous code as much as possible. This was key in working on each stage while constantly being conscious of there being a next stage. Directly putting in certain hardcoded numbers could work fine for the current stage but would limit adaptation in the next stage.

## How did I apply it?

In Stage 2 one of my Critic rules was to +10 satisfaction if served within 2 turns of ordering. That works for the Pasta dish which the Critic always defaulted to. However, reviewing it, the 2-turn rule only worked since the Critic would always order Pasta because it was the most expensive item at the time. This would no longer hold true if there was a more expensive dish or option added to the menu, and if that took a minimum of more than 2 turns to serve, the +10 satisfaction would be unreachable, making it less valuable to prioritise the Critic. So, I changed the threshold to follow the dish ordered. Thus, in Stage 3 when combos were added, the CriticCustomer class did not need to be modified since the rule works for any dish, including combos.

## What challenged me?

One part I found challenging was in Stage 3, specifically where the Host needed the same seating rules as the Waiter, while HappyHour needed a different price in takeOrder(), which was in Waiter. I had a few options between copying the seating code from Waiter into Host which would have left duplicated code and changing Waiter’s existing methods which risked breaking the controller’s calls and invalidating the previous test runs.

## How did I solve it?


In the end, I moved the seating logic into the base class Staff as protected method seatFromQueue(), which let both Waiter and Host call it. Then I overloaded the original 3-argument takeOrder() with a 4-argument one, which allowed the controller to continue working while my teammate switched to the 4-argument version. Lastly, to check nothing broke, I replayed the Stage 1 defeat game before and after the change and checked that the output stayed the same, including 4 refused seats, and tested the new behavior using WaiterTest and HostTest. The trade-off is that other classes like Chef and Cashier also inherit seatFromQueue() despite not using it. However, I found this preferable over the other methods’ drawbacks. From this I learned how to manage changes to working code, focusing on keeping old entry points and testing to show the changes are harmless.

Footnote: The satisfaction possibly being unreachable and the SeatingStaff design idea was suggested by Claude (Anthropic) during code review and the reflection’s wording was proofread by Claude (Anthropic).
