# Sequence diagram: VIP Customer #2 (Stage 3)

![Sequence diagram for VIP Customer #2, Stage 3 victory run, turns 4-7](vip2-sequence.png)

**Stage and scenario:** Stage 3, VIP Customer #2 in the recorded victory run (`docs/test-runs/stage3-victory.txt`), turns 4–7, then the final result check.

**Starting conditions:** turn 4, Happy Hour not used yet, #1 at Table 1 ready to pay, Table 2 free, and VIP #2 arrives in the queue with satisfaction 100.

## The interactions in order

**Turn 4:** the controller calls `ConsoleUI.askChoice()` and the manager picks Yes, so `HappyHour.activate(4)` runs (alt). Inside opt [happyHour.isActive], `recoverSatisfaction(15)` keeps #2 at 100. The Host calls `seat()`, which seats #2 at Table 2. The alt frame shows the other choices: Wait, or a failed action that shows the reason and asks again. The Waiter's `takeOrder()` has the customer pick a Burger ($15.00). `computeBillCents` gives $13.50 with the VIP discount, then `HappyHour.apply` takes 20% off to make it $10.80, which is locked into the new `Order`. The Chef's `prepare()` calls `advancePreparation()`, and the order is READY.

**Turn 5:** inside opt [happyHour.isActive] again, satisfaction recovers from 95 to 100. The Waiter's `serve()` marks the order SERVED, and #2 starts EATING.

**Turn 6:** `finishEatingIfDone(6)` makes #2 READY_TO_PAY.

**Turn 7:** the Cashier's `collectPayment()` checks `canPay(7)` and marks the order and the customer PAID. Then, inside opt [paid != null], `recordPayment()` raises revenue from $9.00 to $19.80.

**After turn 18:** `isVictory()` checks the paid, revenue and satisfaction targets in `GameConfig`, and `showFinalResult()` prints VICTORY.

## Outcome

VIP #2 pays $10.80 at satisfaction 100, and the game ends in VICTORY (5 paid, $77.10, 85.2).

## Left out

Actions for other customers are not shown, to keep the diagram readable. On turn 4 that's #1 paying $9.00. On turn 7 it's the Host seating #3, the Waiter taking #3's order and the Chef starting on it. Turns 8–18 are also left out; the loop frame stands for them.
