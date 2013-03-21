package no.ntnu.gruppe47;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

import no.ntnu.gruppe47.db.Database;
import no.ntnu.gruppe47.db.entities.Alarm;
import no.ntnu.gruppe47.db.entities.Alert;
import no.ntnu.gruppe47.db.entities.Appointment;
import no.ntnu.gruppe47.db.entities.Group;
import no.ntnu.gruppe47.db.entities.Invitation;
import no.ntnu.gruppe47.db.entities.Room;
import no.ntnu.gruppe47.db.entities.User;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class CalendarSystem {
	User user;
	Scanner input = new Scanner(System.in);
	
	public int readInt()
	{
		while (true)
		{
			try
			{
				System.out.print("> ");
				int i = input.nextInt();
				input.nextLine();
				return i;
			}
			catch (Exception e)
			{
				System.out.println("Prøv på nytt");
				input.nextLine();
			}
		}
	}
	
	public void setup()
	{
		Database.reset();
		
		User u1 = User.create("User 1", "password", "Testperson en", "epost1");
		User u2 = User.create("User 2", "password", "Testperson to", "epost2");
		User u3 = User.create("User 3", "password", "Testperson tre", "epost3");
		User u4 = User.create("User 4", "password", "Testperson fire", "epost4");
		User u5 = User.create("User 5", "password", "Testperson fem", "epost5");
		
		Room.create("Rom 1", 2);
		Room.create("Rom 2", 5);
		Room.create("Rom 3", 10);
		
		long unixTime = new DateTime().getMillis();

		Appointment a1 = Appointment.create(u1, new Timestamp(unixTime), new Timestamp(unixTime + 3600 * 1000), "Påskeferie", "Fjellet");

		a1.inviteUser(u2);
		a1.inviteUser(u3);
		a1.inviteUser(u4);
		
		u2.getInvitationsWithResponse(0).get(0).accept();
		u3.getInvitationsWithResponse(0).get(0).reject();

		Appointment a2 = Appointment.create(u1, new Timestamp(unixTime), new Timestamp(unixTime + 3600 * 1000), "Jordens undergang");
		a2.inviteUser(u2);
		a2.inviteUser(u3);
		a2.inviteUser(u4);
		
		Group g = Group.create("Group 1");
		g.addMember(u1);
		g.addMember(u2);
		g.addMember(u3);
		g.addMember(u4);

		Appointment a3 = Appointment.create(u2, new Timestamp(unixTime + 3600 * 2000), new Timestamp(unixTime + 3600 * 3000), "Jordens oppstandelse");
		a3.inviteGroup(g);
	}

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
			System.out.println("4: Restore example database");
			valg = readInt();

			if (valg == 1)
				login();
			else if (valg == 2)
				register();
			else if (valg == 3)
				showUsers();
			else if (valg == 4)
				setup();
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
		System.out.println("=========Login=========");
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

	private void startUpInvitations() {
		if (user.getInvitationsWithResponse(0).size() > 0)
			System.out.println("You have new invitations. Please check up on them.");
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
		while(valg < 0 || valg > 5)
		{
			System.out.println("=========Logged in as " + user.getName() + "=========");
			startUpInvitations();
			System.out.println("0: Log out");
			System.out.println("1: Appointment manager");
			System.out.println("2: Group management");
			System.out.println("3: Invitation manager");
			System.out.println("4: Show alarms");
			System.out.println("5: Show notifications");
			valg = readInt();

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
				invitationManager();
				break;
			case 4:
				showAlarms();
				break;
			case 5:
				showNotifications();
				break;
			}
			valg = -1;
		}
	}

	private void showNotifications() {
		ArrayList<Alert> notifications = user.getAlerts();
		System.out.println("=========Showing notifications=========");
		if (notifications.size() > 0)
			for (Alert a : notifications)
				System.out.println(a);
		else
			System.out.println("You have no notifications");
	}

	private void invitationManager() {
		int valg = -1; 
		while (valg != 0){
			System.out.println();
			System.out.println("=========Invitation manager=========");
			System.out.println("0: Back to main menu");
			System.out.println("1: Show invitations");
			System.out.println("2: Respond to invitations");
			valg = readInt();
			
			switch (valg)
			{
			case 0:
				return;
			case 1:
				showInvitations();
				break;
			case 2:
				respondToInvitations();
				break;
			}
			valg = -1;
		}
	}

	private void respondToInvitations() {
			System.out.println("The invitations will now be listed and you will be prompted to accept (a/A) or reject (r/R):");
			ArrayList<Invitation> invitations = user.getInvitations();
			for (Invitation a : invitations){
				System.out.println(a);
				System.out.println("Accept or recject?");
				System.out.print("> ");
				String svar = input.nextLine();
				if (svar.toLowerCase().equals("a"))
					a.accept();
				else if (svar.toLowerCase().equals("r"))
					a.reject();
				else
					System.out.println("You have not answered, assuming you\'d like to wait to answere.");
			}
	}

	private void appointmentManager(){
		System.out.println("=========Appointment manager=========");
		System.out.println("0: Back to main menu");
		System.out.println("1: Show my appointments weekwise");
		System.out.println("2: Show my appointments this week/month");
		System.out.println("3: Make an appointment");
<<<<<<< HEAD
		System.out.println("4: Edit an appointment");
		System.out.println("4: Delete an appointment");
		System.out.print("> ");

		int valg = input.nextInt();
=======
		int valg = readInt();
		
>>>>>>> b141f6648ac88822eee06ed12bd5d57ef67b7fc2
		while (valg < 0 || valg > 3){
			System.out.println("Your choise was invalid. Please select a valid option.");
			valg = readInt();
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
<<<<<<< HEAD
		case 4:
			editAppointment();
			break;
		case 5:
			deleteAppointment();
			break;
=======
>>>>>>> b141f6648ac88822eee06ed12bd5d57ef67b7fc2
		}
	}

	private void editAppointment() {
		ArrayList<Appointment> appointments = Appointment.getAllFor(user);
		int valg = -2;
		System.out.println("Please select the appointment you want to edit (-1 to go back)");
		for (int i = 0; i < appointments.size(); i++){
			System.out.println(i + ": " + appointments.get(i));
		}
		while (valg <= -2 || valg >= appointments.size()){
			System.out.print("> ");
			valg = input.nextInt();
			input.nextLine();
			if (valg <= -2 || valg >= appointments.size()) System.out.println("Your choise was invalid. Pease try again.");
		}
		if (valg == -1){
			appointmentManager();
			return;
		}
		Appointment edit = appointments.get(valg);
		System.out.println("You have selected" + edit);
		while (valg > 0 && valg <= 3){
			System.out.println("0: cancle.");
			System.out.println("1: edit time");
			System.out.println("2: edit place or room");
			System.out.println("3: edit description");
			valg = input.nextInt();
			input.nextLine();
			switch (valg){
			case 0:
				editAppointment();
				return;
			case 1:
				editTime(edit);
				break;
			case 2:
				editPlaceOrRoom(edit);
				break;
			case 3:
				editDescription(edit);
				break;
			}
		}
	}
	
	

	private void editDescription(Appointment edit) {
		System.out.println("This is the current description: " + edit.getDescription());
		System.out.println("Please enter a new description.");
		System.out.print("> ");
		String description = input.nextLine();
		input.nextLine();
		edit.setDescription(description);
		
	}

	private void editPlaceOrRoom(Appointment edit) {
		if (edit.getPlace() != null){
			System.out.println("The appointment will currently take place at the place of " + edit.getPlace());
		}else System.out.println("The appointment will currently take place in the room named " + Room.getByID(edit.getRoomId()).getRoomNumber());
		
		int valg = -1;
		while (valg >= 0 && valg < 3){
			System.out.println("0: cancle");
			System.out.println("1: change room");
			System.out.println("2: cange place");
			valg = input.nextInt();
			input.nextLine();
			if (valg < 0 || valg > 2) System.out.println("Your choise was invalid. Please try again.");
		}
		switch (valg){
		case 0:
			editAppointment();
			return;
		case 1:
			changeRoom(edit);
			break;
		case 2:
			cangePlace(edit);
			break;
		}
	}
	
	private void editTime(Appointment edit) {
		
		DateTime startT = null;
		DateTime endT = null;
		
		String pattern = "YYYY dd.MM HH:mm";
		DateTimeFormatter fm = DateTimeFormat.forPattern(pattern);

			while (startT == null || endT == null)
			{
		
			System.out.print("Start (dd.mm hh:mm): ");
			String start = input.nextLine();
			System.out.print("End (dd.mm hh:mm): ");
			String end = input.nextLine();

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
		}
		edit.setStartTime(new Timestamp(startT.getMillis()));
		edit.setEndTime(new Timestamp(endT.getMillis()));

	}

	private void cangePlace(Appointment edit) {
		System.out.println("Enter a new place for the appointment");
		System.out.print("> ");
		String place = input.nextLine();
		input.nextLine();
		edit.setPlace(place);
		
	}

	private void changeRoom(Appointment edit) {
		System.out.println("Here is a list of the rooms currently available:");
		ArrayList<Room> rooms = Room.getAvailableRooms(edit.getStartTime(), edit.getEndTime(), edit.getParticipants().size());
		for (int i = 0; i < rooms.size(); i++) {
			System.out.println(i + ": " + rooms.get(i));
		}
		int valg = -1;
		while (valg < 0 || valg >= rooms.size()){
			valg = input.nextInt();
			input.nextLine();
		}
		edit.setRoomId(rooms.get(valg).getRoomId());
	}

	private void groupManagement() {

		int valg = 1;
		while( valg > 0){
			System.out.println("=========Group management=========");
			
			System.out.println("0: Back to main menu");
			System.out.println("1: Make a group");
			System.out.println("2: Add members to a group");
			System.out.println("3: Show my groups");
			System.out.println("4: Show all groups");
			valg = readInt();

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
			valg = readInt();

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
			valg = readInt();
			
			if (valg == 0)
				return;
			if (valg > 0 && valg <= groups.size())
				showGroup(groups.get(valg-1));
			valg = -1;
		}

	}

	private void showAlarms()
	{
<<<<<<< HEAD
		ArrayList<Alarm> alarms = Alarm.getAllAlarmsForUser(user);
		for (int i = 0; i < alarms.size(); i++) {
			System.out.println(alarms.get(i));
		}
	}
=======
		int valg = -1;
		while(valg != 0)
		{
			ArrayList<Alarm> alarms = user.getAlarms();
			System.out.println("=========Showing your alarms=========");
			if (alarms.size() > 0)
				for (int i = 0; i < alarms.size(); i++)
					System.out.println((i+1) + ": " + alarms.get(i));
			else
				System.out.println("You have no alarms");
			System.out.println();

			System.out.println("0: Back");
			if (alarms.size() > 1)
				System.out.println(1 + "-" + alarms.size() + ": Change/delete alarm");
			else if (alarms.size() > 0)
				System.out.println("1: Change/delete alarm");
			valg = readInt();

			if (valg == 0)
				return;
			if (valg >= 1 && valg <= alarms.size())
				showAlarm(alarms.get(valg-1));
			valg = -1;
		}
>>>>>>> b141f6648ac88822eee06ed12bd5d57ef67b7fc2

	}

	private void showAlarm(Alarm alarm) {
		
		int valg = -1;
		while (valg != 0 && valg != 2)
		{
			System.out.println("=========Showing alarm=========");
			System.out.println(alarm);
			System.out.println();
			
			System.out.println("0: Back");
			System.out.println("1: Change time");
			System.out.println("2: Delete");
			valg = readInt();
			
			if (valg == 1)
			{
				Appointment a = Appointment.getByID(alarm.getAppointmentmId());
				DateTime alarmTime = new DateTime(alarm.getTime());
				DateTime appointmentTime = new DateTime(a.getStartTime());
				System.out.println("Avtalen starter " + appointmentTime);
				System.out.println("Hvor mange minutter før avtalen vil du sette alarmen?");
				int tid = readInt();
				
				alarmTime = alarmTime.minusMinutes(tid);
				alarm.setTime(new Timestamp(alarmTime.getMillis()));
				
				valg = -1;
			
			}
			if (valg == 2)
				alarm.delete();
		}
		
	}

	private void showInvitations() {
		ArrayList<Invitation> invitations =	user.getInvitationsWithResponse(0);
				
		if (invitations.size() > 0){
			System.out.println("=========New invitations=========");
			System.out.println("Invitations:");
			for (Invitation i: invitations)
				System.out.println(i);
		}else System.out.println("You have no new invitations.");
		System.out.println();
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
				a = Appointment.create(user, startStamp, endStamp, description, place);
			else
				a = Appointment.create(user, startStamp, endStamp, description);

			if (a == null)
				System.out.println("Something went wrong. try again");

		}
		a.addParticipant(user);

	}

	public void printAppointments() {

		DateTime date = new DateTime();
		int week = date.getWeekOfWeekyear();

		int valg = -1;
		while (valg != 0)
		{
			System.out.println("=========Showing appointments for week " + week + "=========");
			ArrayList<Appointment> appointments = user.getAppointmentsForWeek(week);
			if (appointments.size() > 0)
				for (int i = 0; i < appointments.size(); i++)
					System.out.println((i+1) + ": " + appointments.get(i));
			else
				System.out.println("No appointments in week " + week);
			
			System.out.println();
			System.out.println("-2: Print previous week");
			System.out.println("-1: Print next week");
			System.out.println("0: Back");
			if (appointments.size() > 1)
				System.out.println("1-" + appointments.size() + ": Select appointment");
			else if (appointments.size() > 0)
				System.out.println("1: Select appointment");
			valg = readInt();

			if (valg == 0)
				return;
			else if(valg == -2)
				week--;
			else if (valg == -1)
				week++;
			if (appointments.size() > 0 && valg >= 1 && valg <= appointments.size())
				showAppointment(appointments.get(valg-1));
			valg = -1;
		}

	}

	private void showAppointment(Appointment appointment) {
		
		int valg = -1;
		while (valg != 0 && valg != 1)
		{
			System.out.println("=========Showing appointment=========");
			System.out.println("Description: " + appointment.getDescription());
			System.out.println("Place: " + appointment.getPlace());
			System.out.println("Time: " + appointment.getStartTime() + " - " + appointment.getEndTime());
			System.out.println("Created by: " + User.getByID(appointment.getCreatedBy()).getName());
			System.out.println();

			ArrayList<User> going = appointment.getParticipants();
			ArrayList<User> notGoing = appointment.getInvitesWithResponse(-1);
			ArrayList<User> invited = appointment.getInvitesWithResponse(0);

			System.out.println("Going:");
			for (User u : going)
				System.out.println("\t" + u.getName());
			System.out.println("Not going:");
			for (User u : notGoing)
				System.out.println("\t" + u.getName());
			System.out.println("Invited:");
			for (User u : invited)
				System.out.println("\t" + u.getName());
			System.out.println();

			System.out.println("0: Back");
			System.out.println("1: Delete appointment");
			System.out.println("2: Invite someone");
			valg = readInt();

			if (valg == 1)
				user.deleteAppointment(appointment);
			if (valg == 2)
			{
				inviteToAppointment(appointment);
				valg = -1;
			}
		}
	}

	private void inviteToAppointment(Appointment appointment) {
		int valg = -1;
		while (valg != 0)
		{
			System.out.println("=========Inviting to appointment=========");
			ArrayList<Group> groups = Group.getAll(true);

			for (int i = 0; i < groups.size(); i++)
				System.out.println((i+1) + ": " + groups.get(i));
			System.out.println();

			System.out.println("0: Back");
			if (groups.size() > 1)
				System.out.println(1 + "-" + groups.size() + ": Invite");
			else if (groups.size() > 0)
				System.out.println("1: Invite");
			valg = readInt();

			if (valg == 0)
				return;
			if (valg >= 1 && valg <= groups.size())
				appointment.inviteGroup(groups.get(valg-1));
			valg = -1;
		}
		
	}

	public void showAllGroups()
	{
		ArrayList<Group> groups = Group.getAll(false);

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
			valg = readInt();

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

		ArrayList<Group> groups = Group.getAll(false);
		for (int i = 0; i < groups.size(); i++){
			System.out.println(i + ": " + groups.get(i).getName() +" ("+groups.get(i).getGroupId()+")");
		}
		int selected = readInt();
		while (selected < -1 || selected >= groups.size())
			selected = readInt();
		ArrayList<User> users = User.getAll();
		while (selected >= 0 && selected < users.size()){
			Group group = groups.get(selected);
			System.out.println("Please enter the user ID of the person you want to add to the group:");
			System.out.println("Press -1 for a list of user IDs.");
			System.out.println("Press -2 to go back.");
			selected = readInt();
			if (selected == -1){
				for (User u : users)
					System.out.println("Name: " + u.getName() + " ("+u.getUserId()+")");
			}else if (selected == -2){
				groupManagement();
				return;
			}

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
