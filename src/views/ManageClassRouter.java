package views;

import components.Confirmation;
import components.FormattedTable;
import controller.ClassManagement;
import models.GymClass;
import utils.*;

import java.util.ArrayList;

public class ManageClassRouter {
    private static final UserInterface UI;
    private static final String TITLE = "LIBRARY MANAGEMENT SYSTEM";
    private static final String[] OPTIONS = {"Add New", "Update", "Display", "Delete",  "Return"};

    static {
        UI = new UIBuilder(75)
                .topWall()
                .rightString(TITLE, 5)
                .emptyWall()
                .header("== MANAGE CLASSES ==").withColor("BRIGHT_YELLOW","BLACK")
                .emptyWall()
                .options(OPTIONS).withColor("BRIGHT_WHITE","BLACK")
                .emptyWall()
                .bottomWall()
                .setDefaultColor("BRIGHT_BLUE")
                .saveAndReturn();

    }

    public static int deploy() {
        UI.deploy();
        return Input.getInteger("Your option",1,5);
    }

    public static void createNew(ClassManagement em) {
        do {
            String ID;
            do {
                ID = IntegerID.getWithPrefix("C");
            } while (em.have(ID));
            String name = Input.getString("Input new class's name", true);
            String schedule = Input.getString("Input class's schedule", true);
            int capacity = Input.getInteger("Input capacity", 1, 9999);
            ArrayList<String> members = new ArrayList<>();
            do {
                try {
                    String mID = Input.getStringNoConstraint("Input Member's ID to add");
                    if (mID.isEmpty()) break;
                    if (members.contains(mID))
                        throw new IllegalStateException("Member already exist");
                    if (!em.manageMember().have(mID))
                        throw new IllegalArgumentException("Member not found");
                    else
                        Input.printSuccess("Found user " + em.manageMember().searchWithID(mID).getName());
                    members.add(mID);
                } catch (Exception e) {
                    Input.printError(e.getMessage());
                }
            } while (Confirmation.deploy("Add another Member?"));
            ArrayList<String>  equipments = new ArrayList<>();
            do {
                try{
                    String eId = Input.getStringNoConstraint("Input Equipment's ID to add");
                    if (eId.isEmpty()) break;
                    if (equipments.contains(eId))
                        throw new IllegalStateException("Equipment already exist");
                    if (!em.manageEquipment().have(eId))
                        throw new IllegalStateException("Equipment not found");
                    else
                        Input.printSuccess("Found equipment " + em.manageEquipment().searchWithID(eId).getName());
                    equipments.add(eId);
                } catch (Exception e) {
                    Input.printError(e.getMessage());
                 }
            } while (Confirmation.deploy("Add another Equipment?"));
            em.add(new GymClass(ID, name, schedule, capacity, members, equipments));
        } while (Confirmation.deploy("Add another Class?"));
    }

    public static void update(ClassManagement cm) {
        if (cm.isEmpty()) throw new IllegalStateException("No Class data found.");
        String ID = Input.getString("Enter Class's ID");
        if (!cm.have(ID.toUpperCase()))
            throw new IllegalArgumentException("Invalid ID");
        GymClass preFix = cm.searchWithID(ID);
        GymClass data = new GymClass(preFix.getID(),
                preFix.getName(),
                preFix.getSchedule(),
                preFix.getCapacity(),
                preFix.getMembers(),
                preFix.getEquipments()
        );
        while (true) {
            try {
                switch (UpdateClassRouter.deploy(ID)) {
                    case 1:
                        UpdateClassRouter.adjustName(data);
                        break;
                    case 2:
                        UpdateClassRouter.adjustSchedule(data);
                        break;
                    case 3:
                        UpdateClassRouter.adjustCapacity(data);
                        break;
                    case 4:
                        UpdateClassRouter.adjustMembers(data, cm);
                        break;
                    case 5:
                        UpdateClassRouter.adjustEquipments(data, cm);
                        break;
                    case 6:
                        UpdateClassRouter.confirmChange(cm, data);
                        return;
                    case 7:
                        if (!UpdateClassRouter.antiAccidentLeave()) break;
                        UpdateClassRouter.resetChanges();
                        return;
                }
            } catch (Exception e) {
                Input.printError(e.getMessage());
            }
        }
    }

    public static void display(ClassManagement cm) {
        UIComponents.loadingBarWithColor("Fetching the database...",
                15, 50, "BRIGHT_YELLOW");

        Input.printSuccess("Current Classews:");
        int count = 1;
        int[] sizes = {7, 15, 20, 4, 35,35};
        String[] color2 = {"MAGENTA","MAGENTA", "MAGENTA","MAGENTA","MAGENTA","MAGENTA"};
        FormattedTable ft = new FormattedTable(sizes, color2);
        System.out.print("| No ");
        ft.printWithColor("ClassID,Name,Schedule,Cap,Members,Equipments",",");
        String[] colors = {"BRIGHT_BLUE", "","","","",""};
        ft = new FormattedTable(sizes,colors);
        //main print
        for (GymClass member: cm) {
            System.out.printf("| %2d " ,count);
            ft.printWithColor(member.toString(),"\\|");
            count++;
        }
        Input.enterToContinue();
    }

    public static void delete(ClassManagement cm) {
        String ID = Input.getString("Class ID");
        if (!cm.have(ID)) throw new IllegalStateException("Class not found");
        if (!Confirmation.deploy("Confirm deleting Class with ID " + ID + "?")) return;
        cm.remove(ID);
        System.out.println();
        display(cm);
    }
}
