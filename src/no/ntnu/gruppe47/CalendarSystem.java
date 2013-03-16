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
			System.out.println("5: Show my groups");
			System.out.println("6: Show every group");
			System.out.println("7: Make a group");
			System.out.print("> ");
			valg = input.nextInt();
			input.nextLine();
			
			if (valg == 1)
				printAvtaler();
			else if (valg == 5)
				groupMenu(user.getGroups());
			else if (valg == 6)
				groupMenu(Group.getAll());
			else if (valg == 7)
				createGroup();
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
			System.out.println(">");
			valg = input.nextInt();
			input.nextLine();
			
			if (valg == 0)
				mainMenu();
			else if (valg == 1)
				Print.printToday();
			else if (valg == 2)
				Print.printThisWeek();
			else if (valg == 3)
				Print.printThisMonth();
			else if (valg == 4)
				Print.prntAll();
			else{
				System.out.println("Your choice was invalid. Pleace enter a valid option");
				System.out.println(">");
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
			input.nextLine();
		}
	}
	
	public void createGroup()
	{
		Group group = null;

		System.out.print("Name: ");
		String name = input.nextLine();
			
		group = Group.create(name);
	}
}
