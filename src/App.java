import components.SaveData;
import controller.ClassManagement;
import controller.EquipmentManagement;
import controller.MemberManagement;
import utils.Input;
import views.MainMenuRouter;
import views.ManageClassRouter;
import views.ManageEquipmentRouter;
import views.ManageMemberRouter;

public class App {
    private static final EquipmentManagement EM = new EquipmentManagement();
    private static final MemberManagement MM = new MemberManagement();
    private static final ClassManagement CM = new ClassManagement(MM, EM);


    public static void run() {
        while (true) {
            try {
                switch (MainMenuRouter.deploy()) {
                    case 1:
                        manageMember();
                        break;
                    case 2:
                        manageEquipment();
                        break;
                    case 3:
                        manageClass();
                        break;
                    case 4:
                        report();
                        break;
                    case 5:
                        SaveData.save(MM, EM, CM);
                        break;
                    case 6:
                        Input.printSuccess("Exiting Application...");
                        return;
                }
            } catch (Exception e) {
                Input.printError(e.getMessage());
            }
        }
    }

    private static void manageMember() {
            while (true) {
                try {
                    switch (ManageMemberRouter.deploy()) {
                        case 1:
                            ManageMemberRouter.createNew(MM);
                            break;
                        case 2:
                            ManageMemberRouter.sortAndDisplayByName(MM);
                            break;
                        case 3:
                            ManageMemberRouter.updateMemberInfo(MM);
                            break;
                        case 4:
                            ManageMemberRouter.deleteMember(MM);
                            break;
                        case 5:
                            return;
                    }
                } catch (Exception e) {
                    Input.printError(e.getMessage());
                    Input.enterToContinue();
                }
            }
    }

    private static void manageEquipment() {
        while (true) {
            try {
                switch (ManageEquipmentRouter.deploy()) {
                    case 1:
                        ManageEquipmentRouter.createNew(EM);
                        break;
                    case 2:
                        ManageEquipmentRouter.searchFor(EM);
                        break;
                    case 3:
                        ManageEquipmentRouter.updateEquipmentInfo(EM);
                        break;
                    case 4:
                        ManageEquipmentRouter.sortAndDisplayByName(EM);
                        break;
                    case 5:
                        ManageEquipmentRouter.deleteEquipment(EM);
                        break;
                    case 6:
                        ManageEquipmentRouter.showEquipmentsWithPrice(EM);
                        break;
                    case 7:
                        ManageEquipmentRouter.addPrice(EM);
                        break;
                    case 8:
                        return;
                }
            } catch (Exception e) {
                Input.printError(e.getMessage());
                Input.enterToContinue();
            }
        }
    }

    private static void report() {
        CM.report();
        Input.enterToContinue();
    }

    private static void manageClass() {
        while (true) {
            try {
                switch (ManageClassRouter.deploy()) {
                    case 1:
                        ManageClassRouter.createNew(CM);
                        break;
                    case 2:
                        ManageClassRouter.update(CM);
                        break;
                    case 3:
                        ManageClassRouter.display(CM);
                        break;
                    case 4:
                        ManageClassRouter.delete(CM);
                        break;
                    case 5:
                        return;
                }
            } catch (Exception e) {
                Input.printError(e.getMessage());
                Input.enterToContinue();
            }
        }
    }
}
