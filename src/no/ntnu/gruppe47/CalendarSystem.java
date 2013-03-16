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
			System.out.print("Brukernavn: ");
			String username = input.nextLine();
			System.out.print("Passord: ");
			String password = input.nextLine();
			
			user = Database.login(username, password);
			
			if (user == null)
				System.out.println("Feil brukernavn/passord");

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
			System.out.print("Brukernavn: ");
			String username = input.nextLine();
			System.out.print("Passord: ");
			String password = input.nextLine();
			System.out.print("Navn: ");
			String name = input.nextLine();
			System.out.print("epost: ");
			String email = input.nextLine();
			
			newUser = User.create(username, password, name, email);
			
			if (newUser == null)
				System.out.println("Prøv igjen");
		}
	}
	
	public void mainMenu()
	{
		System.out.println("=========Logged in as " + user.getName() + "=========");
		int valg = -1;
		while(valg < 0)
		{
			System.out.println("0: Logg ut");
			System.out.println("1: Vis avtaler");
			System.out.println("1: Lag avtale");
			System.out.println("2: Vis varsler");
			System.out.println("3: Vis alarmer");
			System.out.println("4: Vis mine grupper");
			System.out.println("5: Vis alle grupper");
			System.out.println("6: Lag gruppe");
			System.out.print("> ");
			valg = input.nextInt();
			input.nextLine();
			
			if (valg == 1)
				printAvtaler();
			else if (valg == 4)
				groupMenu(user.getGroups());
			else if (valg == 5)
				groupMenu(Group.getAll());
			else if (valg == 6)
				createGroup();
		}
	}

	public void printAvtaler() {
		// TODO Skrive ut avtaler
		int valg = 1;
		while (valg >= 0){
			System.out.println("0: Tilbake");
			System.out.println("1: vis avtaler for idag");
			System.out.println("2: vis avtaler for denne uken");
			System.out.println("3: vis avtaler for denne m�neden");
			System.out.println("4: vis alle avtaler");
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
				System.out.println("Valget var ikke gjyldig. Venligst gj�r et nytt valg");
				System.out.println(">");
			}
		}
		
	}

	public void groupMenu(ArrayList<Group> groups)
	{
		System.out.println("=========Viser gruppe=========");
		for (int i = 0; i < groups.size(); i++)
			System.out.println((i+1) + ": " + groups.get(i));

		int valg = 0;
		while(valg >= 0 && valg <= groups.size())
		{
			System.out.println("0: Tilbake");
			if (groups.size() > 0)
				System.out.println(1 + "-" + groups.size() + ": Vis gruppe");
			System.out.print("> ");
			valg = input.nextInt();
			input.nextLine();
		}
	}
	
	public void createGroup()
	{
		Group group = null;

		System.out.print("Navn: ");
		String name = input.nextLine();
			
		group = Group.create(name);
	}
}
