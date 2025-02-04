package models;

import java.util.ArrayList;

public class GymClass {
    private String ID;
    private String name;
    private String schedule;
    private int capacity;
    private final ArrayList<String> members;
    private final ArrayList<String> equipments;

    public GymClass(String ID, String name, String schedule, int capacity, ArrayList<String> members, ArrayList<String> equipments) {
        this.ID = ID;
        this.name = name;
        this.schedule = schedule;
        this.capacity = capacity;
        this.members = members;
        this.equipments = equipments;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public ArrayList<String> getMembers() {
        return members;
    }


    public ArrayList<String>  getEquipments() {
        return equipments;
    }

    public String toString() {
        StringBuilder memberString = new StringBuilder("[");
        StringBuilder equipString =  new StringBuilder("[");
        if (!equipments.isEmpty()) {
            for (int i = 0; i < members.size(); i++) {
                memberString.append(members.get(i));
                if (members.size()-1 != i) memberString.append(",");
            }
        }
        memberString.append("]");
        if (!equipments.isEmpty()) {
            for (int i = 0; i < equipments.size(); i++) {
                equipString.append(equipments.get(i));
                if (equipments.size()-1 != i) equipString.append(",");
            }
        }
        equipString.append("]");
        return String.format("%s|%s|%s|%s|%s|%s", ID, name, schedule, capacity, memberString,equipString);
    }
}
