package restaurant;

import restaurant.game.GameConfig;
import restaurant.game.GameController;
import restaurant.ui.ConsoleUI;

/** Entry point. Builds the base setting and starts the game. */
public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        GameController controller = new GameController(GameConfig.baseSetting(), ui);
        controller.run();
    }
}
