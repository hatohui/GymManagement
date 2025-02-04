package controller;

import models.Member;
import utils.FileIO;

import java.util.ArrayList;
import java.util.Comparator;

public class MemberManagement extends ArrayList<Member> {
    private static final FileIO IO = new FileIO("src/data/Members.txt");
    {
        IO.getState().forEach(this::parseFromString);
    }

    public ArrayList<Member> getSortedByName() {
        ArrayList<Member> sorted = new ArrayList<>(this);
        sorted.sort(Comparator.comparing(Member::getName));
        return sorted;
    }

    public void remove(String ID) {
        if (searchWithID(ID) == null)
            throw new IllegalArgumentException("ID not found.");
        this.removeIf(each -> each.getID().equalsIgnoreCase(ID.trim()));
    }

    public void create(Member member) {
        if (member != null)
            this.add(member);
    }

    public Member searchWithID(String ID) {
        for (Member member : this)
            if (member.getID().equalsIgnoreCase(ID.trim())) {
                return member;
            }
        return null;
    }

    public void parseFromString(String str) {
        if (str.isEmpty())
            throw new IllegalArgumentException("Empty data");
        String[] data = str.trim().split("\\|");
        if (data.length != 5)
            throw new IllegalStateException("Insufficient data");
        Member newMember = new Member(data[0], data[1],data[2],data[3],data[4].trim());
        this.add(newMember);
    }

    public void commit() {
        IO.clear();
        this.forEach(each -> IO.append(each.toString()));
        IO.commit();
    }

    public boolean have(String id) {
        for (Member member : this)
            if (member.getID().equalsIgnoreCase(id.trim())) return true;
        return false;
    }
}
