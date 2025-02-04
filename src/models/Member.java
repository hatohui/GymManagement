package models;

public class Member {
    private String ID;
    private String name;
    private String address;
    private String contact;
    private String membership;

    public Member(String ID, String name, String address, String contact, String membership) {
        this.ID = ID;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.membership = membership;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String toString() {
        return String.format("%s|%s|%s|%s|%s", ID, name, address, contact, membership);
    }
}
