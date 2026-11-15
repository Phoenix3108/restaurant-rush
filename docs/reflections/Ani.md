# Reflection: Ani (Part 4, game loop and console UI)


## What I worked on

I owned Part 4: the game loop and the console interface (`GameController`, `Restaurant`, `GameConfig` and `ConsoleUI`). In Stage 1, I built the turn loop, which runs these steps each turn: arrivals, staff choices, eating and payment, waiting penalties, and the victory check. I also built the full turn display and the final-result screen. Instead of separate Waiter and Chef menus, I wrote one shared `runChoice` retry loop. If the player picks an action that isn't allowed, the staff member's reason is shown and the player is asked again without using up the turn. For Stage 2, I changed customer arrivals so that `GameConfig` holds a list of `Arrival`s, each paired with a `CustomerFactory`. This means the controller never has to check what type of customer arrived. I also recorded the Stage 2 victory run. In Stage 3, I added the Happy Hour prompt and recovery, the Host step, and the new turn order to the loop.

## What went well

- The other parts plugged into the loop without changing their method signatures. That was because we agreed on the timing rules in the README early on.
- Staff methods return an `ActionResult` (success or failure plus a message). This kept the controller simple: it only had to show the message or ask again.
- Making `askChoice` flush its prompt meant we could pipe in the input files. Every test run in `docs/test-runs` can be replayed exactly, which will also help with the video.

## What was difficult

- Getting the order of steps right in Stage 3. The Host has to act before the Waiter's options are built, otherwise the Waiter is offered seats that are already taken.
- Deciding when the Happy Hour price applies. We locked it into the `Order` when it is taken, so the bill doesn't change when Happy Hour ends.
- Coordinating commits so that we dont overwrite each other's work

## What I learned

- how to use git

## What I would do differently

- Java is too verbose for my taste, i prefer python or even JavaScript as I have more experience with those languages
