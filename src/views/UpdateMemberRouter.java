package views;

import components.Confirmation;
import controller.MemberManagement;
import models.Member;
import utils.ColorWrapper;
import utils.Input;
import utils.UIBuilder;
import utils.UserInterface;

import java.util.HashMap;

public class UpdateMemberRouter {
    private static UserInterface UI;
    private static final String TITLE = "LIBRARY MANAGEMENT SYSTEM";
    private static final String[] OPTIONS_2 = {"  Adjust Name  ", "  Adjust Address  ", "Adjust Contact",
            "Adjust Membership", "Confirm Change", "     Return     "};
    private static final HashMap<String, String> changes = new HashMap<>();

    public static void init(String ID) {
        UI = new UIBuilder(75)
                .topWall()
                .rightString(TITLE, 5)
                .emptyWall()
                .header("== ADJUSTING MEMBER DETAILS ==").withColor("BRIGHT_YELLOW", "BLACK")
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
        return Input.getInteger("Your option", 1, 6);
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

    public static void adjustName(Member data) {
        String old = data.getName();
        String name = Input.getStringNoConstraint("Enter new Member's name");
        if (name.isEmpty()) {
            Input.printWarning("No data changed!");
            Input.enterToContinue();
            return;
        }
        data.setName(name);
        changes.put("Name", old + " -> " + data.getName());
    }

    public static void adjustAddress(Member data) {
        String old = data.getAddress();
        String address = Input.getStringNoConstraint("Enter new address");
        if (address.isEmpty()) {
            Input.printWarning("No data changed!");
            Input.enterToContinue();
            return;
        }
        data.setAddress(address);
        changes.put("Address", old + " -> " + address);
    }

    public static void adjustContact(Member data) {
        String old = data.getContact();
        String contact = Input.getStringNoConstraint("Enter new contact");
        if (contact.isEmpty()) {
            Input.printWarning("No data changed!");
            Input.enterToContinue();
            return;
        }
        data.setContact(contact);
        changes.put("Contact", old + " -> " + contact);
    }

    public static void adjustMembership(Member data) {
        String old = data.getMembership();
        String membership = "";
        System.out.println("""
                | 1 |: STANDARD
                | 2 |: PREMIUM
                | 3 |: PRO VIP MAX""");
        switch (Input.getInteger("Your choice",1,3)) {
            case 1 -> membership = "STANDARD";
            case 2 -> membership = "PREMIUM";
            case 3 -> membership = "PRO_VIP_MAX";
        }
        data.setMembership(membership);
        changes.put("Membership", old + " -> " + membership);
    }

    public static void confirmChange(MemberManagement mm, Member data) {
        if (Confirmation.deploy("Confirm changes?"))
        {
            mm.remove(data.getID());
            mm.add(data);
            Input.printSuccess("Successfully changed the data");
            Input.enterToContinue();
        }
    }
}
