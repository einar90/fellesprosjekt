package no.ntnu.gruppe47.db.entities;

import java.util.ArrayList;

public class Group {

    private int groupId;
    private ArrayList<User> memberList;


    public Group(int groupId) {
        this.groupId = groupId;
        memberList = new ArrayList<User>();
    }


    public void addMember(User user) {
        memberList.add(user);
        System.out.println("User " + user + " added to group " + groupId);
    }


    public void removeMember(User user) {
        memberList.remove(user);
        System.out.println(user + " removed from group " + groupId);
    }

    public ArrayList<User> getMemberList() {
        return memberList;
    }

}
