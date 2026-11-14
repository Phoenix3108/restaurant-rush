# Sequence diagram: VIP Customer #2 in the Stage 3 victory run (turns 4 to 7)

Source run: [`docs/test-runs/stage3-victory.txt`](../test-runs/stage3-victory.txt).
Open [`vip2-sequence.html`](vip2-sequence.html) in a browser to view it full size and download it as SVG or PNG.

```mermaid
sequenceDiagram
    autonumber
    actor P as Player
    participant GC as ctl : GameController
    participant UI as ui : ConsoleUI
    participant HH as hh : HappyHour
    participant H as host : Host
    participant W as waiter : Waiter
    participant CH as chef : Chef
    participant CA as cashier : Cashier
    participant V as vip2 : VIPCustomer
    participant R as restaurant : Restaurant
    participant CFG as config : GameConfig

    loop for each turn: playTurn(turn), shown for turns 4 to 7
        Note over GC,R: Turn 4. VIP #35;2 arrives (WAITING, satisfaction 100). Happy Hour not used yet. Table 2 free.

        GC->>UI: askChoice("Switch on Happy Hour now?", [Yes, No])
        UI->>P: print options, read input
        P-->>UI: 1
        UI-->>GC: 0
        alt choice == 0 (Yes)
            GC->>HH: activate(4)
            HH-->>GC: true
            GC->>UI: showMessage("Happy Hour ON: 2 turn(s) left")
        else No
            Note right of GC: Happy Hour stays available for a later turn
        end

        opt happyHour.isActive(4)
            GC->>V: recoverSatisfaction(15)
            V-->>GC: true (stays at 100)
        end

        GC->>UI: askChoice("Host: choose a task", options)
        Note right of UI: Every askChoice prompts the Player the same way (steps 3 to 5)
        UI-->>GC: 1 (Seat #35;2 at Table 2)
        alt action chosen and it succeeds
            GC->>H: seat(vip2, table2, queue)
            H->>V: seatAt(table2)
            Note right of V: SEATED
            H-->>GC: ActionResult.ok
            GC->>UI: showMessage("Host seated Customer #35;2 at Table 2")
        else Wait chosen
            GC->>UI: showMessage("Waits this turn.")
        else action fails, e.g. Table 1 is occupied
            GC->>UI: showError(reason), then askChoice again
        end

        GC->>UI: askChoice("Waiter: choose a task", options)
        UI-->>GC: 0 (Take order of Customer #35;2)
        GC->>W: takeOrder(vip2, menu, 4, hh)
        W->>V: chooseMenuItem(menu)
        V-->>W: Burger ($15.00)
        W->>V: computeBillCents(Burger)
        V-->>W: 1350 (VIP 10% off)
        W->>HH: apply(1350, 4)
        HH-->>W: 1080 (Happy Hour 20% off)
        create participant O as order2 : Order
        W->>O: new Order(vip2, Burger, 1080, 4)
        W->>V: placeOrder(order2)
        Note right of V: ORDERED, bill $10.80 locked in
        W-->>GC: ActionResult.ok
        GC->>UI: showMessage("Customer #35;2 orders Burger ($10.80, Happy Hour)")

        GC->>UI: askChoice("Chef: choose a task", options)
        UI-->>GC: 0 (Prepare Order #35;2)
        GC->>CH: prepare(order2)
        CH->>O: advancePreparation()
        O-->>CH: true (READY, 1/1 units)
        CH-->>GC: ActionResult.ok
        GC->>UI: showMessage("Order #35;2 is now READY")
        Note over GC,V: End of turn 4: waiting penalty, satisfaction 100 to 95

        Note over GC,R: Turn 5. Happy Hour still active (1 turn left).
        opt happyHour.isActive(5)
            GC->>V: recoverSatisfaction(15)
            V-->>GC: true (95 to 100)
        end
        GC->>UI: askChoice("Waiter: choose a task", options)
        UI-->>GC: 0 (Serve Order #35;2)
        GC->>W: serve(order2, 5)
        W->>O: markServed()
        Note right of O: SERVED
        W->>V: serve(5)
        Note right of V: EATING
        W-->>GC: ActionResult.ok
        GC->>UI: showMessage("Waiter served Burger to Customer #35;2")

        Note over GC,R: Turn 6. Happy Hour over. Host, Waiter and Chef all Wait.
        GC->>V: finishEatingIfDone(6)
        V-->>GC: true
        Note right of V: READY_TO_PAY
        GC->>UI: showMessage("Customer #35;2 finished eating and is ready to pay.")

        Note over GC,R: Turn 7. The Cashier acts automatically.
        GC->>CA: collectPayment(activeCustomers, 7)
        CA->>V: canPay(7)
        V-->>CA: true
        CA->>O: markPaid()
        CA->>V: markPaid()
        Note right of V: PAID, table released
        CA-->>GC: vip2
        opt paid != null
            GC->>R: recordPayment(vip2)
            Note right of R: revenue $9.00 to $19.80
            GC->>UI: showMessage("Cashier: Customer #35;2 paid $10.80 (satisfaction 100)")
        end
    end

    Note over GC,CFG: After turn 18: result check
    GC->>GC: isVictory()
    GC->>CFG: isPaidTargetMet(restaurant)
    CFG-->>GC: true (5 paid, target 4)
    GC->>CFG: isRevenueTargetMet(restaurant)
    CFG-->>GC: true ($77.10, target $45.00)
    GC->>CFG: isSatisfactionTargetMet(restaurant)
    CFG-->>GC: true (85.2, target 60)
    GC->>UI: showFinalResult(true, restaurant, config)
    UI->>P: VICTORY
```

## What the diagram shows

This diagram follows VIP Customer #2 through the **Stage 3** victory run, from arrival on turn 4 to payment on turn 7. At the start of turn 4, #2 joins the queue with satisfaction 100. Happy Hour has not been used yet, Table 1 is held by Customer #1 (ready to pay), and Table 2 is free. The player switches on Happy Hour. While it is active, the controller lets waiting customers recover satisfaction (the `opt [happyHour.isActive]` frame). The Host seats #2 at Table 2, the Waiter takes the order, and the Chef finishes the one-unit Burger, all on turn 4. The price is set when the order is taken: the VIP gets 10% off the $15.00 Burger ($13.50), then Happy Hour takes another 20% off, so a bill of **$10.80** is locked into the `Order`. On turn 5 the Waiter serves it, and recovery brings #2 back from 95 to 100. On turn 6 #2 finishes eating and becomes `READY_TO_PAY`. On turn 7 the Cashier automatically collects $10.80, which raises revenue from $9.00 to $19.80. **Outcome:** #2 pays at full satisfaction (100), and the run ends in **VICTORY** with 5 paid, $77.10 revenue and 85.2 average satisfaction, meeting all three targets.

The `GameController` drives every step. Each turn it calls the staff in a fixed order (Happy Hour, Host, Waiter, Chef, then eating and payment). Each staff step builds a list of options and asks the player through `ConsoleUI.askChoice()`. The `alt` frame shows what happens next: a successful action prints its message, "Wait" skips the turn, and a failed action (such as seating at an occupied table) shows the staff member's reason and asks again. The staff objects only change state through `Customer` and `Order` methods (`seatAt`, `placeOrder`, `advancePreparation`, `serve`, `markPaid`), so the customer moves through WAITING, SEATED, ORDERED, EATING, READY_TO_PAY and PAID, and the order through IN_PREPARATION, READY, SERVED and PAID. The `HappyHour` object is used in two places: the controller checks it for recovery, and the Waiter uses it for the discounted price. After the last turn, `isVictory()` checks each target in `GameConfig` and `ConsoleUI.showFinalResult()` prints the result.
