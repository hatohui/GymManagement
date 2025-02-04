package models;

public class Equipment {
    private String ID;
    private String name;
    private String type;
    private int quantity;
    private String condition;
    private float price;

    public Equipment(String ID, String name, String type, int quantity, String condition) {
        this.ID = ID;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.condition = condition;
        this.price = 0;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String toStringWithPrice() {
        return this + "|" + this.getPrice();
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s|%s",ID,name,type,quantity,condition);
    }
}
