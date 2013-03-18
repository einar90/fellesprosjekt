package no.ntnu.gruppe47;

import java.util.ArrayList;
import java.util.Scanner;

import no.ntnu.gruppe47.db.Database;
import no.ntnu.gruppe47.db.entities.Group;
import no.ntnu.gruppe47.db.entities.User;


public class CalendarSystem {
	User user;
	Scanner input = new Scanner(System.in);
	
	public static void main(String[] args) {
		CalendarSystem cs = new CalendarSystem();
		cs.welcomeMenu();
	}
	
	public void welcomeMenu()
	{
		System.out.println("=========Welcome=========");
		int valg = 0;
		while (valg != 1)
		{
			System.out.println("1: Login");
			System.out.println("2: Register user");
			System.out.println("3: List users");
			System.out.print("> ");
			valg = input.nextInt();
			input.nextLine();

			if (valg == 2)
				register();
			else if (valg == 3)
				showUsers();
		}
		
		if (valg == 1)
			login();
	}
	
	private void showUsers() {
		ArrayList<User> users = User.getAll();
		for (User u : users)
			System.out.println(u);
		
	}

	public void login()
	{
		System.out.println("=========Logging in=========");
		while(user == null)
		{
			System.out.print("Username: ");
			String username = input.nextLine();
			System.out.print("Password: ");
			String password = input.nextLine();
			
			user = Database.login(username, password);
			
			if (user == null)
				System.out.println("Wrong username or password");

			mainMenu();
			user = null;
		}
	}
	
	public void register()
	{
		System.out.println("=========Register user=========");
		User newUser = null;
		while(newUser == null)
		{
			System.out.print("Username: ");
			String username = input.nextLine();
			System.out.print("Password: ");
			String password = input.nextLine();
			System.out.print("Name: ");
			String name = input.nextLine();
			System.out.print("E-mail address: ");
			String email = input.nextLine();
			
			newUser = User.create(username, password, name, email);
			
			if (newUser == null)
				System.out.println("There was an error with the given information \nPeace try again");
		}
	}
	
	public void mainMenu()
	{
		System.out.println("=========Logged in as " + user.getName() + "=========");
		int valg = -1;
		while(valg < 0)
		{
			System.out.println("0: Log out");
			System.out.println("1: Show appointments");
			System.out.println("2: Make an appointment");
			System.out.println("3: Show notifications");
			System.out.println("4: Show alarms");
			System.out.println("5: Group management");
			System.out.print("> ");
			valg = input.nextInt();
			input.nextLine();
			
			if (valg == 1)
				printAvtaler();
			else if (valg == 5)
				groupManagement();
		}
	}

	private void groupManagement() {
		System.out.println("=========Group management=========");
		
		int valg = 1;
		while( valg > 0){
			System.out.println("0: Back to main menu");
			System.out.println("1: Make a group");
			System.out.println("2: Add members to a group");
			System.out.println("3: Show my groups");
			System.out.println("4: Show all groups");
			System.out.print("> ");
			valg = input.nextInt();
			
			if (valg == 0)
				mainMenu();
			else if (valg == 1)
				createGroup();
			else if (valg == 2)
				addMember();
			else if (valg == 3)
				groupMenu(user.getGroups());
			else if (valg == 4)
				groupMenu(Group.getAll());
		}
		
	}

	public void printAvtaler() {
		// TODO Skrive ut avtaler
		int valg = 1;
		while (valg >= 0){
			System.out.println("0: Back to main menu");
			System.out.println("1: Show appointments for today");
			System.out.println("2: Show appointments for this week");
			System.out.println("3: Show appointments for this month");
			System.out.println("4: Show all appointments");
			System.out.print("> ");
			valg = input.nextInt();
			input.nextLine();
			
			if (valg == 0)
				mainMenu();
			else if (valg == 1)
				Print.printToday(user);
			else if (valg == 2)
				Print.printThisWeek(user);
			else if (valg == 3)
				Print.printThisMonth(user);
			else if (valg == 4)
				Print.printAll(user);
			else{
				System.out.println("Your choice was invalid. Pleace enter a valid option");
				System.out.print(">");
			}
		}
		
	}

	public void groupMenu(ArrayList<Group> groups)
	{
		System.out.println("=========Showing groups=========");
		for (int i = 0; i < groups.size(); i++)
			System.out.println((i+1) + ": " + groups.get(i));

		int valg = 0;
		while(valg >= 0 && valg <= groups.size())
		{
			System.out.println("0: Back");
			if (groups.size() > 0)
				System.out.println(1 + "-" + groups.size() + ": Show group");
			System.out.print("> ");
			valg = input.nextInt();
//			input.nextLine();
		}
	}
	
	public void createGroup()
	{
		System.out.println("=========Create a group=========");
		System.out.print("Name: ");
		String name = input.nextLine();
		while (name == null || name.equals(""))
			name = input.nextLine();
			
		if (Group.create(name) != null)
			System.out.println("The group named " + name + " was successfully created.");
		System.out.println();
	}
	
	public void addMember()
	{
		System.out.println("========Adding members========");
		System.out.println("Please select the group you want to add members to:");
		System.out.println("-1: Back to group management");
		
		ArrayList<Group> groups = user.getGroups();
		for (int i = 0; i < groups.size(); i++){
			System.out.println(i + ": " + groups.get(i).getName() +" ("+groups.get(i).getGroupId()+")");
		}
		System.out.print("> ");
		int selected = input.nextInt();
		while (selected < -1 || selected >= groups.size())
			selected = input.nextInt();
		ArrayList<User> users = User.getAll();
		while (selected >= 0 && selected < users.size()){
			Group group = groups.get(selected);
			System.out.println("Please enter the user ID of the person you want to add to the group:");
			System.out.println("Press -1 for a list of user IDs.");
			System.out.println("Press -2 to go back.");
			System.out.print("> ");
			selected = input.nextInt();
			if (selected == -1){
				for (User u : users)
					System.out.println("Name: " + u.getName() + " ("+u.getUserId()+")");
			}else if (selected == -2){
				groupManagement();
				return;
			}
			
			System.out.print("> ");
			if (group.addMember(User.getByID(input.nextInt())))
				System.out.println("The user was successfully added the the group.");
			else System.out.println("Either you don't have administer-priviliges to this group or somthing went wrong back there..");
		}
		
		if (selected == -1){
			groupManagement();
			return;
		}
	}
}
