package no.ntnu.gruppe47.db.entities;


public class Group {

    private int groupId;
    private String name;


    public int getGroupId() {
		return groupId;
	}


	public String getName() {
		return name;
	}


	public Group(int groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }
}
