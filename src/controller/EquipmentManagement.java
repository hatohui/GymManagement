package controller;

import models.Equipment;
import utils.FileIO;

import java.util.ArrayList;
import java.util.Comparator;

public class EquipmentManagement extends ArrayList<Equipment> {
    private static final FileIO IO = new FileIO("src/data/Equipments.txt");
    {
        IO.getState().forEach(this::parseFromString);
    }

    public ArrayList<Equipment> getSortedByName() {
        ArrayList<Equipment> sorted = new ArrayList<>(this);
        sorted.sort(Comparator.comparing(Equipment::getName));
        return sorted;
    }

    public void remove(String ID) {
        if (searchWithID(ID) == null)
            throw new IllegalArgumentException("ID not found.");
        this.removeIf(each -> each.getID().equalsIgnoreCase(ID.trim()));
    }

    public void create(Equipment equipment) {
        if (equipment != null)
            this.add(equipment);
    }

    public Equipment searchWithID(String ID) {
        for (Equipment equipment : this)
            if (equipment.getID().equalsIgnoreCase(ID.trim())) {
                return equipment;
            }
        return null;
    }

    public void parseFromString(String str) {
        if (str.isEmpty())
            throw new IllegalArgumentException("Empty data");
        String[] data = str.trim().split("\\|");
        if (data.length != 5)
            throw new IllegalStateException("Insufficient data");
        Equipment newEquipment = new Equipment(data[0], data[1],data[2],
                Integer.parseInt(data[3]),data[4].trim());
        this.add(newEquipment);
    }

    public void commit() {
        IO.clear();
        this.forEach(each -> IO.append(each.toString()));
        IO.commit();
    }

    public boolean have(String id) {
        for (Equipment equipment : this)
            if (equipment.getID().equalsIgnoreCase(id.trim())) return true;
        return false;
    }

    public int check(String ID, int amount) {
        if (searchWithID(ID).getQuantity() < amount)
            throw new IllegalArgumentException("We don't have that many equipments");
        return amount;
    }

    public void addPrice(float price, String condition) {
        if (price < 0) throw new IllegalArgumentException("Invalid price range");

        for (Equipment e : this) {
            if (e.getCondition().equalsIgnoreCase(condition.trim())) {
                e.setPrice(price);
            }
        }
    }
}
