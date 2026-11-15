# Dylan: AI history

Tool: Claude. One continuous conversation covering all three stages: [dylan-claude-chat.pdf](dylan-claude-chat.pdf)

## Stage 1: Part 3 Staff & Tables (27 Oct 2026)

- **Setup:** Claude read the assignment brief, the briefing slides and the starter code, then summarised the Part 3 TODOs, a suggested order and a review checklist. I wrote all the code myself.
- **Table:** I wrote assign/release. Claude found a missing space in the error message and suggested a null check, which I added.
- **Waiter:** I wrote seat, takeOrder and serve. Claude tested them and suggested:
  - null checks returning fail instead of crashing;
  - formatting the bill with Money.format, which then exposed a $$ bug I fixed;
  - using order.isReady();
  - clearer failure messages;
  - removing an unused import.
- **Chef:** I wrote prepare. Claude suggested calling markActed() after the work rather than before, and fixing a missing space in the message.
- **Cashier:** I wrote collectPayment. Claude tested ordering, tie-breaks and one payment per turn, and suggested else if for readability.
- **Tooling:** how to compile and run the project on macOS.

## Stage 2: CriticCustomer (6–7 Nov 2026)

- **Template:** I planned the tasks. Claude provided a commented template for CriticCustomer with the bodies left empty; I wrote the implementation.
- **Existing code:** Claude confirmed that Customer.serve() already calls the onServed() hook, so Waiter needed no change.
- **Review findings:**
  - Claude tested my version and verified the fastest-service timing.
  - Claude pointed out that the fixed 2-turn threshold would make the +10 unreachable once Stage 3 combos are added, and suggested tying it to the dish's preparation units. I made that change.
- **Workflow:** advice on committing to my branch before my teammate's Menu.getMostExpensive() existed, without merging into main until it does.

## Stage 3: Host, report and declaration (14–15 Nov 2026)

- **Host design:** Claude reviewed my Host plan and test. It suggested building messages with getRole(), keeping the null check in the shared code and adding extra test cases. It also proposed SeatingStaff as an alternative to a helper in Staff.
- **First Host version:** Claude listed its compile errors (wrong method names); I fixed them.
- **Final version:** Claude compiled the full project and ran all the test classes, including my HostTest and WaiterTest. It also replayed the Stage 3 victory and defeat runs against their transcripts, and checked edge cases (null input, double-seating, a second seat in the same turn).
- **Reflection:** Claude reviewed my drafts for accuracy and clarity.
- **Declaration:** Claude suggested where I should apply footnotes.