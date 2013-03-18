package no.ntnu.gruppe47;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

import no.ntnu.gruppe47.db.Database;
import no.ntnu.gruppe47.db.entities.Appointment;
import no.ntnu.gruppe47.db.entities.Group;
import no.ntnu.gruppe47.db.entities.User;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class CalendarSystem {
	User user;
	Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		CalendarSystem cs = new CalendarSystem();
		cs.welcomeMenu();
	}

	public void welcomeMenu()
	{
		int valg = -1;
		while (valg != 0)
		{
			System.out.println("=========Welcome=========");
			System.out.println("0: Exit");
			System.out.println("1: Login");
			System.out.println("2: Register user");
			System.out.println("3: List users");
			System.out.print("> ");
			valg = input.nextInt();
			input.nextLine();

			if (valg == 1)
				login();
			else if (valg == 2)
				register();
			else if (valg == 3)
				showUsers();
			else if (valg == 0)
				return;
		}
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
		}

		mainMenu();
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
				System.out.println("There was an error with the given information \nPlease try again");
		}
	}

	public void mainMenu()
	{
		int valg = -1;
		while(valg < 0)
		{
			System.out.println("=========Logged in as " + user.getName() + "=========");
			System.out.println("0: Log out");
			System.out.println("1: Appointment manager");
			System.out.println("2: Group management");
			System.out.println("3: Show notifications");
			System.out.println("4: Show alarms");
			System.out.print("> ");
			valg = input.nextInt();
			input.nextLine();

			switch (valg)
			{
			case 0:
				user = null;
				return;
			case 1:
				appointmentManager();
				break;
			case 2:
				groupManagement();
				break;
			case 3:
				showNotifications();
				break;
			case 4:
				showAlarms();
				break;
			}
			valg = -1;
		}
	}

	private void appointmentManager(){
		System.out.println("=========Appointment manager=========");
		System.out.println("0: Back to main menu");
		System.out.println("1: Show my appointments weekwise");
		System.out.println("2: Show my appointments this week/month");
		System.out.println("3: Make an appointment");
		System.out.println("4: Delete an appointment");
		System.out.print("> ");

		int valg = input.nextInt();
		while (valg < 0 || valg > 3){
			System.out.println("Your choise was invalid. Please select a valid option.");
			System.out.print("> ");
			valg = input.nextInt();	
		}

		switch (valg){
		case 0:
			return;
		case 1:
			printAppointments();
			break;
		case 2:
			printAvtaler();
			break;
		case 3:
			createAppointment();
			break;
		case 4:
			deleteAppointment();
			break;
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
				return;
			else if (valg == 1)
				createGroup();
			else if (valg == 2)
				addMember();
			else if (valg == 3)
				showUserGroups();
			else if (valg == 4)
				showAllGroups();
		}

	}

	public void printAvtaler() {
		int valg = -1;
		while (valg < 0){
			System.out.println("0: Back to main menu");
			System.out.println("1: Show appointments for today");
			System.out.println("2: Show appointments for this week");
			System.out.println("3: Show appointments for this month");
			System.out.println("4: Show all appointments");
			System.out.print("> ");
			valg = input.nextInt();
			input.nextLine();

			if (valg == 0)
				return;
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
			valg = -1;
		}
	}

	private void showUserGroups() {
		ArrayList<Group> groups = user.getGroups(false);

		int valg = -1;
		while(valg != 0)
		{
			System.out.println("=========Showing all your groups=========");
			for (int i = 0; i < groups.size(); i++)
				System.out.println((i+1) + ": " + groups.get(i));
			System.out.println();

			System.out.println("0: Back");
			if (groups.size() > 1)
				System.out.println(1 + "-" + groups.size() + ": Show group");
			else if (groups.size() > 0)
				System.out.println("1: Show group");
			System.out.print("> ");
			valg = input.nextInt();
			input.nextLine();
		}

	}

	private void showAlarms()
	{
		// TODO Auto-generated method stub

	}

	private void showNotifications() {
		// TODO Auto-generated method stub

	}

	private void deleteAppointment() {
		// TODO Auto-generated method stub

	}

	private void createAppointment() {
		String pattern = "YYYY dd.MM HH:mm";
		DateTimeFormatter fm = DateTimeFormat.forPattern(pattern);

		Appointment a = null;
		while (a == null)
		{
			System.out.print("Description: ");
			String description = input.nextLine();
			System.out.print("Start (dd.mm hh:mm): ");
			String start = input.nextLine();
			System.out.print("End (dd.mm hh:mm): ");
			String end = input.nextLine();
			System.out.print("Do you need a room? (Y/N): ");
			String rom = input.nextLine();
			String place = "";
			if ("N".equals(rom))
			{
				System.out.print("Where will this appointment be held?: ");
				place = input.nextLine();
			}
			else
				System.out.println("A room will be allocated for you");

			DateTime startT = null;
			DateTime endT = null;
			try
			{
				startT = fm.parseDateTime("2013 " + start);
				endT = fm.parseDateTime("2013 " + end);
			}
			catch(Exception e)
			{
				System.out.println("Datoene ble gale, prøv igjen");
				continue;
			}

			Timestamp startStamp = new Timestamp(startT.getMillis());
			Timestamp endStamp = new Timestamp(endT.getMillis());

			if ("N".equals(rom))
				a = Appointment.create(user, startStamp, endStamp, description, "planlagt", place);
			else
				a = Appointment.create(user, startStamp, endStamp, description, "planlagt");

			if (a == null)
				System.out.println("Something went wrong. try again");

		}
		a.addParticipant(user.getPrivateGroup());

	}

	public void printAppointments() {

		DateTime date = new DateTime();
		int week = date.getWeekOfWeekyear();

		int valg = -1;
		while (valg != 0)
		{
			Print.printWeekNum(user, week);
			System.out.println("0: Back");
			System.out.println("1: Print previous week");
			System.out.println("2: Print next week");
			valg = input.nextInt();
			input.nextLine();

			if (valg == 0)
				return;
			else if(valg == 1)
				week--;
			else if (valg == 2)
				week++;
		}

	}

	public void showAllGroups()
	{
		ArrayList<Group> groups = Group.getAll();

		int valg = -1;
		while(valg != 0)
		{
			System.out.println("=========Showing all groups=========");
			for (int i = 0; i < groups.size(); i++)
				System.out.println((i+1) + ": " + groups.get(i));
			System.out.println();

			System.out.println("0: Back");
			if (groups.size() > 1)
				System.out.println(1 + "-" + groups.size() + ": Show group");
			else if (groups.size() > 0)
				System.out.println("1: Show group");
			System.out.print("> ");
			valg = input.nextInt();

			if (valg == 0)
				return;
			if (valg >= 1 && valg <= groups.size())
				showGroup(groups.get(valg-1));
			valg = -1;
		}
	}

	private void showGroup(Group group) {
		System.out.println("=========Showing group=========");
		System.out.println("Name: " + group);
		ArrayList<User> users = group.getMembers();
		System.out.println("Medlemmer:");
		for(User u : users)
			System.out.println(u.getName());
		System.out.println();

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

		ArrayList<Group> groups = user.getGroups(false);
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
