package views;

import components.Confirmation;
import controller.ClassManagement;
import models.GymClass;
import utils.ColorWrapper;
import utils.Input;
import utils.UIBuilder;
import utils.UserInterface;
import java.util.HashMap;

public class UpdateClassRouter {
    private static UserInterface UI;
    private static final String TITLE = "LIBRARY MANAGEMENT SYSTEM";
    private static final String[] OPTIONS_2 = {"  Adjust Name  ", "  Adjust Schedule  ", "Adjust Capacity",
            "Adjust Members", "Adjust Equipments", "Confirm Change", "     Return     "};
    private static final HashMap<String, String> changes = new HashMap<>();

    public static void init(String ID) {
        UI = new UIBuilder(75)
                .topWall()
                .rightString(TITLE, 5)
                .emptyWall()
                .header("== ADJUSTING CLASS DETAILS ==").withColor("BRIGHT_YELLOW", "BLACK")
                .header("Current ID: " + ID).withColor("BRIGHT_GREEN")
                .emptyWall()
                .options(OPTIONS_2).withColor("BRIGHT_WHITE", "BLACK")
                .emptyWall()
                .bottomWall()
                .setDefaultColor("BRIGHT_BLUE")
                .saveAndReturn();
    }

    public static int deploy(String id) {
        init(id);
        UI.deploy();
        if (!changes.isEmpty()) {
            System.out.print(ColorWrapper.addColor("CURRENT CHANGES:\n","RED"));
            getChanges();
        }
        return Input.getInteger("Your option", 1, 8);
    }

    private static void getChanges() {
        StringBuilder toPrint = new StringBuilder();
        for (String key : changes.keySet()) {
            toPrint.append(" ")
                    .append(ColorWrapper.addColor(key, "BRIGHT_GREEN"))
                    .append(": ")
                    .append(ColorWrapper.addColor(changes.get(key),"BRIGHT_YELLOW", "BLACK")).append("\n");
        }
        Input.separator();
        System.out.print(toPrint);
    }

    public static void resetChanges() {
        changes.clear();
    }

    public static boolean antiAccidentLeave() {
        if (changes.isEmpty()) return true;
        return Confirmation.deploy("You have unsaved changes? Confirm to leave?");
    }

    public static void adjustName(GymClass data) {
        String old = data.getName();
        String name = Input.getStringNoConstraint("Enter new Class's name");
        if (name.isEmpty()) {
            Input.printWarning("No data changed!");
            Input.enterToContinue();
            return;
        }
        data.setName(name);
        changes.put("Name", old + " -> " + data.getName());
    }

    public static void adjustSchedule(GymClass data) {
        String old = data.getSchedule();
        String name = Input.getStringNoConstraint("Enter new schedule");
        if (name.isEmpty()) {
            Input.printWarning("No data changed!");
            Input.enterToContinue();
            return;
        }
        data.setSchedule(name);
        changes.put("Schedule", old + " -> " + data.getName());
    }

    public static void adjustCapacity(GymClass data) {
        int old = data.getCapacity();
        int name = Input.getInteger("Enter new capacity (-1 to quit)", -1, 9999);
        if (name == -1) {
            Input.printWarning("No data changed!");
            Input.enterToContinue();
            return;
        }
        data.setCapacity(name);
        changes.put("Capacity", old + " -> " + data.getCapacity());
    }

    public static void adjustMembers(GymClass data, ClassManagement cm) {
        while (true) {
            try {
                System.out.println("""
                        MEMBERS:
                        | 1 |: ADD
                        | 2 |: REMOVE
                        | 3 |: RETURN""");

                switch (Input.getInteger("Your option", 1, 3)) {
                    case 1:
                        String id = Input.getStringNoConstraint("Input user's ID");
                        if (data.getMembers().contains(id))
                            throw new IllegalArgumentException("This member already exist in the class");
                        if (!cm.manageMember().have(id))
                            throw new IllegalArgumentException("This member does not exist");
                        data.getMembers().add(id);
                        Input.printSuccess("Added data");
                        changes.put("added Member", id);
                        break;
                    case 2:
                        String id2 = Input.getStringNoConstraint("Input user's ID");
                        if (!data.getMembers().contains(id2))
                            throw new IllegalArgumentException("This does not exist in the class");
                        if (!cm.manageMember().have(id2))
                            throw new IllegalArgumentException("This member does not exist");
                        data.getMembers().remove(id2);
                        Input.printSuccess("Removed data");
                        changes.put("removed Member", id2);
                        break;
                    case 3:
                        return;
                }
            } catch (Exception e) {
                Input.printError(e.getMessage());
            }
        }
    }

    public static void confirmChange(ClassManagement cm, GymClass data) {
        if (Confirmation.deploy("Confirm changes?"))
        {
            cm.remove(data.getID());
            cm.add(data);
            Input.printSuccess("Successfully changed the data");
            Input.enterToContinue();
        }
    }

    public static void adjustEquipments(GymClass data, ClassManagement cm) {
        while (true) {
            try {
                System.out.println("""
                        EQUIPMENTS:
                        | 1 |: ADD
                        | 2 |: REMOVE
                        | 3 |: RETURN""");

                switch (Input.getInteger("Your option", 1, 3)) {
                    case 1:
                        String id = Input.getStringNoConstraint("Input equipment's ID");
                        if (data.getEquipments().contains(id))
                            throw new IllegalArgumentException("This equipment already exist in the class");
                        if (!cm.manageEquipment().have(id))
                            throw new IllegalArgumentException("This equipment does not exist");
                        data.getEquipments().add(id);
                        Input.printSuccess("Added data");
                        changes.put("added Equipment", id);
                        break;
                    case 2:
                        String id2 = Input.getStringNoConstraint("Input equipment's ID");
                        if (!data.getEquipments().contains(id2))
                            throw new IllegalArgumentException("This does not exist in the class");
                        if (!cm.manageEquipment().have(id2))
                            throw new IllegalArgumentException("This equipment does not exist");
                        data.getEquipments().remove(id2);
                        Input.printSuccess("Removed data");
                        changes.put("removed Equipment", id2);
                        break;
                    case 3:
                        return;
                }
            } catch (Exception e) {
                Input.printError(e.getMessage());
            }
        }
    }
}
