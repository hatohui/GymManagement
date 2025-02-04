package controller;

import models.Equipment;
import models.GymClass;
import models.Member;
import utils.FileIO;
import utils.Input;

import java.util.*;

public class ClassManagement extends ArrayList<GymClass> {
    private static MemberManagement MEMBER_MANAGEMENT;
    private static EquipmentManagement EQUIPMENT_MANAGEMENT;
    private static final FileIO IO = new FileIO("src/data/Classes.txt");
    {
        IO.getState().forEach(this::parseFromString);
    }

    public ClassManagement(MemberManagement mm, EquipmentManagement em) {
        MEMBER_MANAGEMENT = mm;
        EQUIPMENT_MANAGEMENT = em;
    }

    public MemberManagement manageMember() {
        return MEMBER_MANAGEMENT;
    }

    public EquipmentManagement manageEquipment() {
        return EQUIPMENT_MANAGEMENT;
    }

    public ArrayList<GymClass> getSortedByName() {
        ArrayList<GymClass> sorted = new ArrayList<>(this);
        sorted.sort(Comparator.comparing(GymClass::getName));
        return sorted;
    }

    public void remove(String ID) {
        if (searchWithID(ID) == null)
            throw new IllegalArgumentException("ID not found.");
        this.removeIf(each -> each.getID().equalsIgnoreCase(ID.trim()));
    }

    public void create(GymClass equipment) {
        if (equipment != null)
            this.add(equipment);
    }

    public GymClass searchWithID(String ID) {
        for (GymClass equipment : this)
            if (equipment.getID().equalsIgnoreCase(ID.trim())) {
                return equipment;
            }
        return null;
    }

    public void parseFromString(String str) {
        if (str.isEmpty())
            throw new IllegalArgumentException("Empty data");
        String[] data = str.trim().split("\\|");
        if (data.length != 6)
            throw new IllegalStateException("Insufficient data");
        String mIDs = data[4].substring(1, data[4].length() - 1);
        System.out.println(mIDs);
        ArrayList<String> members = new ArrayList<>(Arrays.asList(mIDs.split(",")));
        String eIDs = data[5].strip().substring(1, data[5].length() - 1);
        ArrayList<String> equipments = new ArrayList<>(Arrays.asList(eIDs.split(",")));
        System.out.println(eIDs);
        GymClass newEquipment = new GymClass(data[0], data[1],data[2],
               Integer.parseInt(data[3]),members, equipments);
        this.add(newEquipment);
    }

    public void commit() {
        IO.clear();
        this.forEach(each -> IO.append(each.toString()));
        IO.commit();
    }

    public boolean have(String id) {
        for (GymClass equipment : this)
            if (equipment.getID().equalsIgnoreCase(id.trim())) return true;
        return false;
    }

    public void report() {
        GymClass mostPopularClass = this.get(0);

        HashMap<String, Integer> count = new HashMap<>();
        int totalMembers = 0;


        for (GymClass each: this) {
            if (each.getMembers().size() > mostPopularClass.getMembers().size()) {
                mostPopularClass = each;
            }
            totalMembers += each.getMembers().size();
        }

        for (GymClass c : this) {
            c.getEquipments().forEach(equipment -> {
                if (count.containsKey(equipment)) {
                    count.put(equipment, count.get(equipment) + 1);
                } else count.put(equipment, 0);
            });
        }

        String max = (String) count.keySet().toArray()[0];
        for (String key : count.keySet()) {
            if (count.get(key) > count.get(max)) {
                max = key;
            }
        }

        Input.printSuccess("REPORT: \n\t" + mostPopularClass.getName()
                + " is the most popular class" + "\n\t" + EQUIPMENT_MANAGEMENT.searchWithID(max).getName()
                + " is the most popular equipment" + "\n\t"
                + "Revenue generated is $" + (60*totalMembers) + " USD"
        );
    }
}

