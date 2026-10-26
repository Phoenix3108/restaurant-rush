# Restaurant Rush

SC2002 group project: a turn-based restaurant management game in Java (command line).

## Build and run

Requires JDK 17 or newer. Run from the repo root.

**macOS / Linux**
```
./run.sh
```

**Windows**
```
run.bat
```

Manual equivalent:
```
javac -d out $(find src -name "*.java")
java -cp out restaurant.Main
```

## Who owns what (Stage 1)

| Part | Package / files | Owner |
|---|---|---|
| 1 Menu & Orders | `restaurant.menu`: `MenuItem`, `Menu`, `Order`, `OrderStatus`, `Money` | Guru |
| 2 Customers | `restaurant.customer`: `Customer`, `CustomerState`, `WaitingQueue` | Ram |
| 3 Staff & Tables | `restaurant.staff`: `Staff`, `Waiter`, `Chef`, `Cashier`, `ActionResult`; `restaurant.floor`: `Table` | Dylan |
| 4 Game loop & CLI | `restaurant.game`: `GameController`, `Restaurant`, `GameConfig`; `restaurant.ui`: `ConsoleUI` | Ani |

Each method you own is marked `// TODO(Part N)`. Search the code for your part number.
**Don't rename or change the signature of a method someone else calls** without telling the group first.

## Timing rules everyone must follow

These rules involve more than one part. Get them right and the parts will fit together.

- **Waiter acts before Chef.** An order can be served only if it was already READY when the Waiter acts, so a dish finished this turn is served next turn at the earliest.
- **Chef can cook an order taken this same turn.**
- **Eating takes one full turn.** Served on turn *t* → `EATING`; on turn *t+1* (step 4) → `READY_TO_PAY`; pays on turn *t+2* at the earliest.
- **Cashier:** at most one payment per turn, oldest `READY_TO_PAY` first. Payment marks order and customer PAID, frees the table and adds the bill to revenue.
- **Satisfaction loss (step 5):** only `WAITING`, `SEATED` and `ORDERED` customers lose 8 points (min 0). At 0 they leave: cancel the unfinished order, free the table, remove from the queue.
- **Invalid choice:** return `ActionResult.fail(reason)`, don't call `markActed()`, and let the manager choose again.
- **Money:** always integer cents; format with `Money.format(cents)`.
- **Bill is locked when the order is taken** (`Order.getBillCents()`).

Worked example (brief 3.3): a customer arrives turn 1 and is seated (92), orders Pasta turn 2 (84), Pasta becomes READY turn 3 (76), is served turn 4 (76), becomes READY_TO_PAY turn 5, and pays $18.00 turn 6.

## Git workflow

1. `git pull` on `main` before starting.
2. Work on your own branch: `part1-menu-orders`, `part2-customers`, `part3-staff-tables`, `part4-game-loop`.
3. Commit often with clear messages, push, open a Pull Request into `main`.
4. Make sure `./run.sh` compiles before you open the PR.
5. **No Stage 2 code in `main` until `stage-1` is tagged.** Same for Stage 3 and `stage-2`.

Tagging (once a stage is finished and tested):
```
git tag stage-1
git push origin stage-1
```

## Documented rules

- Food choice rule: customers cycle through the menu by ID: #1 Salad, #2 Burger, #3 Pasta, #4 Salad, #5 Burger, #6 Pasta.
- Display order: the turn display is shown after step 1 (arrivals) so the manager can see the new customer before choosing.

## Stage comparisons

To be filled in for 1 → 2 and 2 → 3 (new files, changed files with reasons, compare links).

## Video timestamps

To be filled in.
