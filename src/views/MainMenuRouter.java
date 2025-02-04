package views;

import utils.Input;
import utils.UIBuilder;
import utils.UserInterface;

public class MainMenuRouter {
    private static final UserInterface UI;
    private static final String TITLE = "LIBRARY MANAGEMENT SYSTEM";
    private static final String[] OPTIONS = {"Manage Members","Manage Equipments", "  Manage Class  "
            ,"    Reports    ","     Save      ","     Exit     "};
    static {
        UI = new UIBuilder(75)
                .topWall()
                .rightString(TITLE, 5)
                .emptyWall()
                .header("== MAIN MENU ==").withColor("BRIGHT_YELLOW","BLACK")
                .emptyWall()
                .options(OPTIONS).withColor("BRIGHT_WHITE","BLACK")
                .emptyWall()
                .bottomWall()
                .setDefaultColor("BRIGHT_BLUE")
                .saveAndReturn();
    }

    public static int deploy() {
        UI.deploy();
        return Input.getInteger("Your option:", 1, 6);
    }
}
