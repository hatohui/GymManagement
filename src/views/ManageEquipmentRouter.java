package views;

import components.Confirmation;
import components.FormattedTable;
import controller.EquipmentManagement;
import models.Equipment;

import utils.*;

public class ManageEquipmentRouter {
    private static final UserInterface UI;
    private static final String TITLE = "LIBRARY MANAGEMENT SYSTEM";
    private static final String[] OPTIONS = {"Add New", "Search ", "Update", "Display","Delete", "Show Price", "Add Price",  "Return"};

    static {
        UI = new UIBuilder(75)
                .topWall()
                .rightString(TITLE, 5)
                .emptyWall()
                .header("== MANAGE EQUIPMENTS ==").withColor("BRIGHT_YELLOW","BLACK")
                .emptyWall()
                .options(OPTIONS).withColor("BRIGHT_WHITE","BLACK")
                .emptyWall()
                .bottomWall()
                .setDefaultColor("BRIGHT_BLUE")
                .saveAndReturn();

    }

    public static int deploy() {
        UI.deploy();
        return Input.getInteger("Your option",1,8);
    }

    public static void createNew(EquipmentManagement em) {
        do {
            String ID;
            do {
                ID = IntegerID.getWithPrefix("E");
            } while (em.have(ID));
            String name = Input.getString("Input new equipment's name", true);
            String type = Input.getString("Input equipment's type", true);
            int quantity = Input.getInteger("Input quantity", 9999);
            String condition = "";
            System.out.println("""
                | 1 |: NEW
                | 2 |: MODERATE
                | 3 |: BROKEN""");
            switch (Input.getInteger("Your choice",1,3)) {
                case 1 -> condition = "NEW";
                case 2 -> condition = "MODERATE";
                case 3 -> condition = "BROKEN";
            }
            em.create(new Equipment(ID, name, type, quantity, condition));
        } while (Confirmation.deploy("Add another Equipment?"));
    }


    public static void updateEquipmentInfo(EquipmentManagement mm) {
        if (mm.isEmpty()) throw new IllegalStateException("No Member data found.");
        String ID = Input.getString("Enter Member's ID");
        if (!mm.have(ID.toUpperCase()))
            throw new IllegalArgumentException("Invalid ID");
        Equipment preFix = mm.searchWithID(ID);
        Equipment data = new Equipment(preFix.getID(),
                preFix.getName(),
                preFix.getType(),
                preFix.getQuantity(),
                preFix.getCondition()
        );
        while (true) {
            try {
                switch (UpdateEquipmentRouter.deploy(ID)) {
                    case 1:
                        UpdateEquipmentRouter.adjustName(data);
                        break;
                    case 2:
                        UpdateEquipmentRouter.adjustType(data);
                        break;
                    case 3:
                        UpdateEquipmentRouter.adjustQuantity(data);
                        break;
                    case 4:
                        UpdateEquipmentRouter.adjustCondition(data);
                        break;
                    case 5:
                        UpdateEquipmentRouter.confirmChange(mm, data);
                        return;
                    case 6:
                        if (!UpdateEquipmentRouter.antiAccidentLeave()) break;
                        UpdateEquipmentRouter.resetChanges();
                        return;
                }
            } catch (Exception e) {
                Input.printError(e.getMessage());
            }
        }
    }

    public static void deleteEquipment(EquipmentManagement mm) {
        String ID = Input.getString("Equipment ID");
        if (!mm.have(ID)) throw new IllegalStateException("Equipment not found");
        if (!Confirmation.deploy("Confirm deleting Equipment with ID " + ID + "?")) return;
        mm.remove(ID);
        System.out.println();
        UIComponents.loadingBarWithColor("Fetching the database...",
                20, 100, "BRIGHT_YELLOW");
        System.out.println(Input.successMessage("Successfully updated the database"));
        showAll(mm);
    }

    private static void showAll(EquipmentManagement em) {
        Input.printSuccess("Current Equipments:");
        int count = 1;
        int[] sizes = {7, 15, 10, 4, 8};
        String[] color2 = {"MAGENTA","MAGENTA", "MAGENTA","MAGENTA","MAGENTA"};
        FormattedTable ft = new FormattedTable(sizes, color2);
        System.out.print("| No ");
        ft.printWithColor("EquID,Name,Type,Quan,Cond",",");
        String[] colors = {"BRIGHT_BLUE", "", "", "", "COND"};
        ft = new FormattedTable(sizes,colors);
        //main print
        for (Equipment member: em) {
            System.out.printf("| %2d " ,count);
            ft.printWithColor(member.toString(),"\\|");
            count++;
        }
        Input.enterToContinue();
    }

    public static void addPrice(EquipmentManagement EM) {
        float price = Input.getFloat("Input new price");
        String condition = Input.getString("Input conditions");
        EM.addPrice(price, condition);
    }

    public static void showEquipmentsWithPrice(EquipmentManagement EM) {
        Input.printSuccess("Current Equipments:");
        int count = 1;
        int[] sizes = {7, 15, 10, 4, 8, 6};
        String[] color2 = {"MAGENTA","MAGENTA", "MAGENTA","MAGENTA","MAGENTA", "MAGENTA"};
        FormattedTable ft = new FormattedTable(sizes, color2);
        System.out.print("| No ");
        ft.printWithColor("EquID,Name,Type,Quan,Cond,Price",",");
        String[] colors = {"BRIGHT_BLUE", "", "", "", "COND",""};
        ft = new FormattedTable(sizes,colors);
        //main print
        for (Equipment member: EM) {
            System.out.printf("| %2d " ,count);
            ft.printWithColor(member.toStringWithPrice(),"\\|");
            count++;
        }
        Input.enterToContinue();
    }

    public static void showEquipment(EquipmentManagement EM, String ID) {
        Input.printSuccess("Current Equipment:");
        int[] sizes = {7, 15, 10, 4, 8};
        String[] color2 = {"MAGENTA","MAGENTA", "MAGENTA","MAGENTA","MAGENTA"};
        FormattedTable ft = new FormattedTable(sizes, color2);
        ft.printWithColor("EquID,Name,Type,Quantity,Condition",",");
        String[] colors = {"BRIGHT_BLUE", "", "", "", "COND"};
        ft = new FormattedTable(sizes,colors);
        ft.printWithColor(EM.searchWithID(ID).toString(),"\\|");
        Input.enterToContinue();
    }

    public static void sortAndDisplayByName(EquipmentManagement em) {
        UIComponents.loadingBarWithColor("Sorting and fetching the database...",
                15, 50, "BRIGHT_YELLOW");
        Input.printSuccess("Current Equipments:");
        int count = 1;
        int[] sizes = {7, 15, 10, 4, 8};
        String[] color2 = {"MAGENTA","MAGENTA", "MAGENTA","MAGENTA","MAGENTA"};
        FormattedTable ft = new FormattedTable(sizes, color2);
        System.out.print("| No ");
        ft.printWithColor("EquID,Name,Type,Quan,Cond",",");
        String[] colors = {"BRIGHT_BLUE", "", "", "", "COND"};
        ft = new FormattedTable(sizes,colors);
        //main print
        for (Equipment equipment: em.getSortedByName()) {
            System.out.printf("| %2d " ,count);
            ft.printWithColor(equipment.toString(),"\\|");
            count++;
        }
        Input.enterToContinue();
    }

    public static void searchFor(EquipmentManagement em) {
        String id = Input.getString("Input ID");
        if (!em.have(id)) throw new IllegalArgumentException("ID not found.");
        showEquipment(em, id);
    }
}
