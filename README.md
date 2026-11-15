# Restaurant Rush

SC2002 group project: a turn-based restaurant management game in Java (command line).

**Repository:** https://github.com/Phoenix3108/restaurant-rush

| Stage | Tag (runnable snapshot) | Adds |
|---|---|---|
| 1 Basic operation | [`stage-1`](https://github.com/Phoenix3108/restaurant-rush/tree/stage-1) | Regular customers, menu, Waiter, Chef, Cashier, full turn sequence, both endings |
| 2 Customer behaviour | [`stage-2`](https://github.com/Phoenix3108/restaurant-rush/tree/stage-2) | VIP and Critic customers |
| 3 New role and menu policy | [`stage-3`](https://github.com/Phoenix3108/restaurant-rush/tree/stage-3) | Host, ComboMeal, Happy Hour |

To run an earlier stage: `git checkout stage-1` (or `stage-2`), then `./run.sh` (Windows: `.\run.bat`). `git checkout main` returns to Stage 3.

## Build and run

Requires JDK 17 or newer. Run from the repo root.

**macOS / Linux**
```
./run.sh
```

**Windows** (PowerShell, e.g. the VS Code terminal)
```
.\run.bat
```

Manual equivalent:
```
javac -d out $(find src -name "*.java")
java -cp out restaurant.Main
```

## Who owns what

| Part | Owner | Stage 1 | Stage 2 | Stage 3 |
|---|---|---|---|---|
| 1 Menu & Orders | Guru | `MenuItem`, `Menu`, `Order`, `OrderStatus`, `Money` | `Money.percentOff()`, `Menu.getMostExpensive()` | `ComboMeal`, `HappyHour` |
| 2 Customers | Ram | `Customer`, `CustomerState`, `WaitingQueue` | Abstract `Customer`, `RegularCustomer`, `VIPCustomer` | Combo choice rule in `chooseMenuItem()`, `recoverSatisfaction()` for Happy Hour, Stage 3 test runs |
| 3 Staff & Tables | Dylan | `Staff`, `Waiter`, `Chef`, `Cashier`, `ActionResult`, `Table` | `CriticCustomer`, `WaiterTest` | `Host`, shared `Staff.seatFromQueue()`, Happy Hour price in `Waiter.takeOrder()`, `HostTest` |
| 4 Game loop & CLI | Ani | `GameController`, `Restaurant`, `GameConfig`, `ConsoleUI` | `Arrival`, `CustomerFactory`, VIP/Critic arrivals, type display | Happy Hour prompt and recovery step, Host step |

## Timing rules everyone must follow

These rules involve more than one part, so every part follows them.

- **Waiter acts before Chef.** An order can be served only if it was already READY when the Waiter acts, so a dish finished this turn is served next turn at the earliest.
- **Chef can cook an order taken this same turn.**
- **Eating takes one full turn.** Served on turn *t* → `EATING`; on turn *t+1* (step 4) → `READY_TO_PAY`; pays on turn *t+2* at the earliest.
- **Cashier:** at most one payment per turn, oldest `READY_TO_PAY` first. Payment marks order and customer PAID, frees the table and adds the bill to revenue.
- **Satisfaction loss (step 5):** only `WAITING`, `SEATED` and `ORDERED` customers lose satisfaction (Regular 8, VIP 5, Critic 12) with minimum 0 points. At 0 they leave: cancel the unfinished order, free the table, remove from the queue.
- **Invalid choice:** return `ActionResult.fail(reason)`, don't call `markActed()`, and let the manager choose again.
- **Money:** always integer cents; format with `Money.format(cents)`.
- **Bill is locked when the order is taken** (`Order.getBillCents()`).

Worked example (brief 3.3): a customer arrives turn 1 and is seated (92), orders Pasta turn 2 (84), Pasta becomes READY turn 3 (76), is served turn 4 (76), becomes READY_TO_PAY turn 5, and pays $18.00 turn 6.

## Git workflow

1. `git pull` on `main` before starting.
2. Work on your own branch (e.g. `guruparan/part1-menu-orders`, `part2-customers`, `ani/game-loop`), never directly on `main`.
3. Commit often with clear messages, push, open a Pull Request into `main`.
4. Make sure `./run.sh` compiles before you open the PR.
5. **No Stage 2 code in `main` until `stage-1` is tagged.** Same for Stage 3 and `stage-2`.

Tagging (once a stage is finished and tested):
```
git tag stage-1
git push origin stage-1
```

## Documented rules

- Food choice rule: every third customer (#3, #6) orders the Salad + Pasta combo ($24.30, 3 units); everyone else cycles through the single dishes by ID: #1 Salad, #2 Burger, #4 Salad, #5 Burger. The Critic overrides this and always orders the most expensive single dish (Pasta). (In Stages 1 and 2, before combos existed, #3 and #6 ordered Pasta.)
- Happy Hour recovery: on each active turn, customers still waiting for a table or food (WAITING, SEATED, ORDERED) recover 15 satisfaction at turn start, up to 100; customers already served are unaffected.
- Host (Stage 3): seats at most 1 queued customer per turn, in addition to the Waiter's one task (the Waiter can still seat too). The Host acts before the Waiter. Host and Waiter use the same seating rules (`Staff.seatFromQueue`): a refused choice (customer not in queue, table occupied) does not use up the Host's task.
- Display order: the turn display is shown after step 1 (arrivals) so the manager can see the new customer before choosing.
- Turn order (Stage 3):
  1. All four staff (Waiter, Chef, Cashier, Host) start a new turn.
  2. Turn header, arrivals, then the turn display.
  3. Happy Hour prompt "Switch on Happy Hour now?" (Yes/No), only while it has not been used.
  4. If Happy Hour is active this turn, every customer still waiting for a table or food recovers 15 satisfaction (`GameConfig.HAPPY_HOUR_RECOVERY`).
  5. Host choice, then Waiter choice (its options are built after the Host acts), then Chef choice.
  6. Eating and payment, waiting penalty and leaving, then the end-of-turn summary (unchanged).
- Customer types (Stage 2), one subclass of the abstract `Customer` each; the controller never checks the type:

  | Type | Satisfaction lost per waiting turn | Bill | Special |
  |---|---|---|---|
  | Regular (`RegularCustomer`) | 8 | Full price | None |
  | VIP (`VIPCustomer`) | 5 | 10% off, rounded to the nearest cent, locked when the order is taken (Burger $13.50) | None |
  | Critic (`CriticCustomer`) | 12 | Full price | Always orders the most expensive item (Pasta on the base menu). When served: +10 satisfaction if served as fast as the kitchen allows (serve turn − turn the order was taken ≤ the dish's preparation units, e.g. Pasta is 2 turns), otherwise −10. Kept within 0–100. A served Critic never leaves, even at 0: they still eat and pay. |

## Test notes

### Stage 1

| Run | Inputs | Transcript | Result |
|---|---|---|---|
| Victory | `docs/test-runs/stage1-victory-input.txt` | `docs/test-runs/stage1-victory.txt` | 5 paid, $66.00, avg 79.2 → VICTORY |
| Defeat | `docs/test-runs/stage1-defeat-input.txt` | `docs/test-runs/stage1-defeat.txt` | 0 paid, 2 unhappy departures → DEFEAT |

The victory run serves a READY dish when there is one, otherwise takes an order, otherwise seats the next customer; the Chef always cooks the oldest order.
The defeat run also exercises invalid input (turn 1: `fghjkl`, `9`, `0`) and invalid choices (turn 7: seating at an occupied table is refused with a reason and the Waiter chooses again).

### Stage 2

Base setting with a VIP on turn 4 and a Critic on turn 10 (the other four are Regular).

| Run | Inputs | Transcript | Result |
|---|---|---|---|
| Victory | `docs/test-runs/stage2-victory-input.txt` | `docs/test-runs/stage2-victory.txt` | 5 paid, $73.50, avg 77.6 → VICTORY |

Same strategy as the Stage 1 victory run. The VIP (#2) orders a Burger billed at $13.50 (10% off) and loses only 5 per waiting turn; the Critic (#4) orders Pasta, is served as fast as the kitchen allows and gets +10 (52 → 62).

### Stage 3

Base setting with the Host, combos (#3 and #6) and Happy Hour.

| Run | Inputs | Transcript | Result |
|---|---|---|---|
| Victory | `docs/test-runs/stage3-victory-input.txt` | `docs/test-runs/stage3-victory.txt` | 5 paid, $77.10, avg 85.2 → VICTORY |
| Defeat | `docs/test-runs/stage3-defeat-input.txt` | `docs/test-runs/stage3-defeat.txt` | 0 paid, 2 unhappy departures → DEFEAT |

Victory run: the Host seats every arrival, then the Waiter serves a READY dish, else takes an order, else seats; the Chef cooks the oldest order. Happy Hour is switched on at turn 4: the VIP (#2) recovers to 100 and orders a Burger at $10.80 (10% VIP, then 20% Happy Hour, the brief's example). #3 orders the Salad + Pasta combo ($24.30, 3 units) after Happy Hour has ended, so it pays full combo price. The Critic (#4) orders Pasta and gets +10 for fast service.
Defeat run: every staff member waits and Happy Hour is never used; #1 leaves at the end of turn 13 and the Critic (#4, 12 per turn) at the end of turn 18.
For comparison, playing the same way without using the Host (and without Happy Hour) gives only 4 paid customers ($64.80), so the Host lets one more customer finish in 18 turns.

Each input file has one choice per line. To replay a run exactly:

**Windows (PowerShell)**
```
javac -d out -sourcepath src src\restaurant\Main.java
Get-Content docs\test-runs\stage3-victory-input.txt | java -Dreplay=true -cp out restaurant.Main
```

**macOS / Linux**
```
javac -d out -sourcepath src src/restaurant/Main.java
java -Dreplay=true -cp out restaurant.Main < docs/test-runs/stage3-victory-input.txt
```

`-Dreplay=true` makes the game echo each input, so the output matches the transcript.
Stage 1 runs match their transcripts only on the `stage-1` tag (`git checkout stage-1`), because Stage 2 changes the customers on turns 4 and 10.
Stage 2 runs match only on the `stage-2` tag (`git checkout stage-2`), because Stage 3 adds the Happy Hour and Host prompts, which change the inputs.

### Unit tests

Each test class has its own `main` and prints PASS/FAIL. `./run.sh` compiles the tests along with the game, but `run.bat` compiles only the game, so compile the test you want first (works on both):
```
javac -d out -sourcepath src src/restaurant/customer/CustomerTest.java
java -cp out restaurant.customer.CustomerTest
```
Same for `menu.MenuTest`, `menu.MoneyTest`, `menu.OrderTest`, `menu.ComboMealTest`, `menu.HappyHourTest`, `staff.WaiterTest` and `staff.HostTest`. At `stage-3` all 125 checks pass.

## Stage comparisons

### Stage 1 → 2

Compare: https://github.com/Phoenix3108/restaurant-rush/compare/stage-1...stage-2

**New files**

| File | Why |
|---|---|
| `customer/RegularCustomer` | The Stage 1 behaviour as its own type, now that `Customer` is abstract |
| `customer/VIPCustomer` | Loses 5 per waiting turn, 10% off the bill |
| `customer/CriticCustomer` | Loses 12 per waiting turn, orders the most expensive dish, ±10 when served |
| `customer/CustomerTest` | Tests for the three customer types |
| `staff/WaiterTest` | Checks that `Waiter.serve()` triggers the Critic's reaction through the `onServed()` hook (no Critic-specific code in `Waiter`) |

**Changed files**

| File | What changed and why |
|---|---|
| `customer/Customer` | Now abstract, with hooks `getTypeLabel()`, `getSatisfactionLossPerTurn()`, `onServed()`, so each type adds behaviour without changing the controller |
| `game/CustomerFactory` | `REGULAR` uses `RegularCustomer::new` (`Customer` can't be created directly) |
| `menu/Menu` | `getMostExpensive()` for the Critic |
| `menu/Money` | `percentOff()` so all discounts share one rounding rule |
| `game/GameConfig` | VIP on turn 4, Critic on turn 10 (still 6 arrivals, no if-else in the controller) |
| `ui/ConsoleUI` | Shows the customer type |
| `menu/OrderTest`, `menu/MenuTest`, `menu/MoneyTest` | Updated tests |

**What still works? (Stage 1 → 2)**

- **Order lifecycle:** all 25 `OrderTest` checks still pass. Two lines now use `new RegularCustomer(...)`, because `Customer` became abstract.
- **Menu and money:** the 10 `MenuTest` and 6 `MoneyTest` checks from Stage 1 still pass, plus new ones (13 and 10 in total).
- **Regular customers:** same rules as Stage 1 (lose 8 per waiting turn, pay full price), now in `RegularCustomer` and checked by `CustomerTest`.
- **Game loop:** `GameController` did not change. The Stage 2 victory run follows the same timing rules (a dish finished this turn is served next turn at the earliest, eating takes one full turn, one payment per turn).
- **Unit tests:** 41 passing at `stage-1` → 70 passing at `stage-2`, none failing.

**Change map (non-test classes, 21 → 24):** 3 added, 6 modified, 15 unchanged.

- Added: `RegularCustomer`, `VIPCustomer`, `CriticCustomer`
- Modified: `Customer`, `CustomerFactory`, `GameConfig`, `Menu`, `Money`, `ConsoleUI`
- Unchanged: `Main`, `CustomerState`, `WaitingQueue`, `Table`, `Arrival`, `GameController`, `Restaurant`, `MenuItem`, `Order`, `OrderStatus`, `ActionResult`, `Staff`, `Waiter`, `Chef`, `Cashier`. The new customer types needed no change to the game loop or any staff class.

### Stage 2 → 3

Compare: https://github.com/Phoenix3108/restaurant-rush/compare/stage-2...stage-3

**New files**

| File | Why |
|---|---|
| `menu/ComboMeal` | A combo of two dishes with its own price and preparation units |
| `menu/HappyHour` | 20% off for two turns, once per game |
| `staff/Host` | Seats one queued customer per turn, before the Waiter |
| `menu/ComboMealTest`, `menu/HappyHourTest`, `staff/HostTest` | Tests for the above |

**Changed files**

| File | What changed and why |
|---|---|
| `menu/Menu` | Separate combo list, so the Critic still picks Pasta |
| `customer/Customer` | Combo choice rule and `recoverSatisfaction()` for Happy Hour |
| `staff/Staff`, `staff/Waiter` | Shared seating via `seatFromQueue()` (used by the Host too); Happy Hour price in `takeOrder()` |
| `game/Restaurant` | Holds the Host and the HappyHour |
| `game/GameController` | Happy Hour prompt, recovery, Host step before the Waiter |
| `game/GameConfig` | `HAPPY_HOUR_RECOVERY` constant (15) |
| `ui/ConsoleUI` | Happy Hour status line and the Host in "Tasks available" |
| Tests | Updated for the above |

**What still works? (Stage 2 → 3)**

- **Order lifecycle:** all 25 `OrderTest` checks still pass, unchanged.
- **Menu and money:** all earlier checks still pass; `getMostExpensive()` still returns Pasta even with combos added, so the Critic is unaffected.
- **Customer types:** all 20 Stage 2 `CustomerTest` checks still pass (31 in total). The VIP still pays $13.50 for a Burger outside Happy Hour, and the Critic's +10 for fast service still passes in `WaiterTest`.
- **Seating:** moving the seating rules from `Waiter` into `Staff.seatFromQueue()` (shared with the Host) did not change the Waiter's behaviour: the Stage 1 defeat input, replayed on the code just before and just after this change (before the Stage 3 prompts were added), gave identical output for all 18 turns, including the refused seat at an occupied table. `WaiterTest` still passes after the change.
- **Game loop:** same six steps; the Happy Hour prompt and the Host step are added before the Waiter. The old three-argument `takeOrder()` still works and never applies Happy Hour.
- **Unit tests:** 70 passing at `stage-2` → 125 passing at `stage-3`, none failing.

**Change map (non-test classes, 24 → 27):** 3 added, 8 modified, 16 unchanged.

- Added: `ComboMeal`, `HappyHour`, `Host`
- Modified: `Customer`, `GameConfig`, `GameController`, `Restaurant`, `Menu`, `Staff`, `Waiter`, `ConsoleUI`
- Unchanged: `Main`, `CustomerState`, `WaitingQueue`, `RegularCustomer`, `VIPCustomer`, `CriticCustomer`, `Table`, `Arrival`, `CustomerFactory`, `MenuItem`, `Money`, `Order`, `OrderStatus`, `ActionResult`, `Chef`, `Cashier`. Combos needed no change to `Order`, `Chef` or `Cashier`, because `ComboMeal` is a `MenuItem`.

Note: each stage's recorded runs replay exactly only on their own tag, because later stages change the inputs on purpose (different customers on turns 4 and 10 in Stage 2; new Happy Hour and Host prompts, and customer #3 ordering a combo, in Stage 3).

## AI history

Full AI conversations on each member's own branch:

| Member | AI history |
|---|---|
| Dylan | [`dylan/ai-history`](https://github.com/Phoenix3108/restaurant-rush/tree/dylan/ai-history/ai-history) (full chat as PDF, with a stage index) |

## Video

Link: https://youtu.be/oHtRYOT1uCg

| # | Segment | Speaker | Start |
|---|---|---|---|
| 1 | The game launching from the project, overview of the three stages | Dylan | 0:00 |
| 2 | Manager choices when staff are constrained (refused seat, Waiter's trade-off) | Dylan | 1:24 |
| 3 | A dish going READY → SERVED → payment | Guru | 2:10 |
| 4 | VIP and Critic behaviour | Ram | 3:17 |
| 5 | Stage 3: Host | Dylan | 4:50 |
| 6 | Stage 3: ComboMeal and Contribution: Guru| Guru | 5:03 |
| 7 | One victory and one defeat (replays from `docs/test-runs`) | Ram | 5:24 |
| 8 | Class diagrams for each stage | Guru | 6:16 |
| 9 | Class diagrams: conclusion | Guru | 8:13 |
| 10 | Stage changes and change map | Ram | 8:28 |
| 11 | Sequence diagram, including Happy Hour (VIP #2, turns 4–7) and Contribution: Ani | Ani | 9:47 |
| 12 | Contribution: Dylan | Dylan | 13:39 |
| 13 | Contribution: Ram | Ram | 14:05 |