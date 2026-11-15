# Reflection: Ram (Part 2, customers; repo owner)

## What I worked on

I owned Part 2, the customers: `Customer`, `CustomerState`, `WaitingQueue`, and from Stage 2 `RegularCustomer` and `VIPCustomer`. I also set up the repository, reviewed and merged everyone's pull requests, tagged `stage-1`, `stage-2` and `stage-3`, and recorded the victory and defeat runs in `docs/test-runs`.

## What did I learn?

- **Abstract classes with hooks.** In Stage 2 I turned `Customer` into an abstract class. The shared rules (the state machine, satisfaction, waiting penalty) stay in `Customer`, and each type only overrides small hooks: `getSatisfactionLossPerTurn()`, `computeBillCents()`, `chooseMenuItem()` and `onServed()`. Before this project I mostly used inheritance to reuse code. Here I learned to use it to let the parent class decide *when* something happens and the subclass decide *what* happens.
- **Protecting rules inside a class.** Making `adjustSatisfaction()` `final` and keeping the 0–100 limit inside it means no subclass can break that rule, even one written by someone else (Dylan's `CriticCustomer`).
- **Polymorphism instead of type checks.** The game loop never asks what type a customer is. There is no `instanceof` anywhere in the project.
- **Working on a shared codebase:** branches, pull requests, merge order and tags.

## How did I apply it?

- When the VIP and Critic were added in Stage 2, `GameController` did not change at all. It still calls `applyWaitingPenalty()`, and each customer object uses its own loss rate (8, 5 or 12). That is the Open/Closed Principle in our code, not just in a definition.
- `requireState()` throws an `IllegalStateException` if, for example, a customer is served before ordering. Real bugs fail loudly, while a wrong choice by the player returns an `ActionResult` and is simply asked again.
- In Stage 3 I added the combo rule to `chooseMenuItem()` (customers whose ID is a multiple of 3 order the Salad + Pasta combo) and `recoverSatisfaction()` for Happy Hour, which only affects customers who have not been served yet (waiting, seated or ordered). `CustomerTest` grew from 20 checks in Stage 2 to 31 in Stage 3.

## What challenged me?

- **Changing a class other people already used.** Making `Customer` abstract broke code that created `new Customer(...)` directly, such as `OrderTest`.
- **Parts depending on each other.** Dylan's Critic needed Guru's `Menu.getMostExpensive()`, so his branch would not compile until Guru's was merged. In Stage 3, if the combo had been added to the main menu list, the Critic's "most expensive dish" would have become the $24.30 combo instead of Pasta.
- **Making the game testable from a file.** The run script broke because our folder path has spaces in it, and the replay output did not show the inputs, so the recorded runs were hard to read.

## How did I solve it?

- I replaced every direct `new Customer(...)` with `new RegularCustomer(...)` and wrote the change into the README, so the team knew why `Customer` had changed.
- I merged pull requests in dependency order (Guru's menu first, then Dylan's Critic) and checked that the game compiled and the tests passed after each merge. For the combo problem, the menu keeps combos in a separate list, so `getMostExpensive()` still returns Pasta.
- I fixed `run.bat` with `-sourcepath`, and added a `-Dreplay=true` option that echoes each input. Then I replayed every recorded run on its own tag and checked the output matched line for line. That gave us a quick way to prove that earlier behaviour still worked after each stage.
