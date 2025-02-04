package components;

import controller.ClassManagement;
import controller.EquipmentManagement;
import controller.MemberManagement;
import utils.Input;
import utils.UIComponents;

public class SaveData {
    public static void save(MemberManagement member, EquipmentManagement equipments, ClassManagement gymClass) {
        try {
            member.commit();
            equipments.commit();
            gymClass.commit();
            UIComponents.loadingBarWithColor("Saving Data...",
                    40, 50, "BRIGHT_GREEN");
            System.out.print(Input.successMessage("Data saved successfully!"));
            Thread.sleep(4000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
