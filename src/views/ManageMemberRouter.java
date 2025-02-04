package views;

import components.Confirmation;
import components.FormattedTable;
import controller.MemberManagement;
import models.Member;
import utils.*;

public class ManageMemberRouter {
    private static final UserInterface UI;
    private static final String TITLE = "LIBRARY MANAGEMENT SYSTEM";
    private static final String[] OPTIONS = {"Add New", "Display", "Update", "Delete", "Return"};

    static {
        UI = new UIBuilder(75)
                .topWall()
                .rightString(TITLE, 5)
                .emptyWall()
                .header("== MANAGE MEMBER ==").withColor("BRIGHT_YELLOW","BLACK")
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

    public static void createNew(MemberManagement mm) {
        do {
            String ID;
            do {
                ID = IntegerID.getWithPrefix("M");
            } while (mm.have(ID));
            String name = Input.getString("Input new member's name",
                    "^[a-zA-Z]+([\\s'-][a-zA-Z]+)*$");
            String address = Input.getString("Input new Member's address", true);
            String contact = Input.getString("Input the user's phone number");
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
            mm.create(new Member(ID, name, address, contact, membership));
        } while (Confirmation.deploy("Add another Member?"));
    }

    public static void sortAndDisplayByName(MemberManagement mm) {
        UIComponents.loadingBarWithColor("Fetching the database...",
                15, 50, "BRIGHT_YELLOW");
        Input.printSuccess("Current Members:");
        int count = 1;
        int[] sizes = {7, 15, 25, 15, 11};
        String[] color2 = {"MAGENTA","MAGENTA", "MAGENTA","MAGENTA","MAGENTA"};
        FormattedTable ft = new FormattedTable(sizes, color2);
        System.out.print("| No ");
        ft.printWithColor("MemID,Name,Address,Contact,Membership",",");
        String[] colors = {"BRIGHT_BLUE", "", "", "", "GREEN"};
        ft = new FormattedTable(sizes,colors);
        //main print
        for (Member user: mm.getSortedByName()) {
            System.out.printf("| %2d " ,count);
            ft.printWithColor(user.toString(),"\\|");
            count++;
        }
        Input.enterToContinue();
    }

    public static void updateMemberInfo(MemberManagement mm) {
        if (mm.isEmpty()) throw new IllegalStateException("No Member data found.");
        String ID = Input.getString("Enter Member's ID");
        if (!mm.have(ID.toUpperCase()))
            throw new IllegalArgumentException("Invalid ID");
        Member preFix = mm.searchWithID(ID);
        Member data = new Member(preFix.getID(),
                preFix.getName(),
                preFix.getAddress(),
                preFix.getContact(),
                preFix.getMembership()
        );
        while (true) {
            try {
                switch (UpdateMemberRouter.deploy(ID)) {
                    case 1:
                        UpdateMemberRouter.adjustName(data);
                        break;
                    case 2:
                        UpdateMemberRouter.adjustAddress(data);
                        break;
                    case 3:
                        UpdateMemberRouter.adjustContact(data);
                        break;
                    case 4:
                        UpdateMemberRouter.adjustMembership(data);
                        break;
                    case 5:
                        UpdateMemberRouter.confirmChange(mm, data);
                        return;
                    case 6:
                        if (!UpdateMemberRouter.antiAccidentLeave()) break;
                        UpdateMemberRouter.resetChanges();
                        return;
                }
            } catch (Exception e) {
                Input.printError(e.getMessage());
            }
        }
    }

    public static void deleteMember(MemberManagement mm) {
        String ID = Input.getString("Member ID");
        if (!mm.have(ID)) throw new IllegalStateException("Member not found");
        if (!Confirmation.deploy("Confirm deleting Member with ID " + ID + "?")) return;
        mm.remove(ID);
        System.out.println();
        showAll(mm);
        Input.enterToContinue();
    }

    private static void showAll(MemberManagement mm) {
        UIComponents.loadingBarWithColor("Fetching the database...",
                15, 50, "BRIGHT_YELLOW");

        Input.printSuccess("Current Users:");
        int count = 1;
        int[] sizes = {7, 15, 25, 15, 11};
        String[] color2 = {"MAGENTA","MAGENTA", "MAGENTA","MAGENTA","MAGENTA"};
        FormattedTable ft = new FormattedTable(sizes, color2);
        System.out.print("| No ");
        ft.printWithColor("MemID,Name,Address,Contact,Membership",",");
        String[] colors = {"BRIGHT_BLUE", "", "", "", "GREEN"};
        ft = new FormattedTable(sizes,colors);
        //main print
        for (Member member: mm) {
            System.out.printf("| %2d " ,count);
            ft.printWithColor(member.toString(),"\\|");
            count++;
        }
        Input.enterToContinue();
    }
}
