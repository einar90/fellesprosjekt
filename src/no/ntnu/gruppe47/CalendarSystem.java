package no.ntnu.gruppe47;

import java.util.ArrayList;
import java.util.Scanner;

import no.ntnu.gruppe47.db.SQLMotor;
import no.ntnu.gruppe47.db.entities.Group;
import no.ntnu.gruppe47.db.entities.User;


public class CalendarSystem {
	User user;
	SQLMotor sql;
	Scanner input = new Scanner(System.in);
	
	public static void main(String[] args) {
		CalendarSystem cs = new CalendarSystem();
		cs.welcomeMenu();
	}
	
	public CalendarSystem()
	{
		sql = new SQLMotor();
	}
	
	public void welcomeMenu()
	{
		System.out.println("=========Welcome=========");
		int valg = 0;
		while (valg != 1 && valg != 2)
		{
			System.out.println("1. Login");
			System.out.println("2. Register user");
			System.out.print("> ");
			valg = input.nextInt();
		}
		
		if (valg == 1)
			login();
		else if (valg == 2)
			register();
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
			
			user = sql.user.login(username, password);
			
			if (user == null)
				System.out.println("Feil brukernavn/passord");
		}
		mainMenu();
	}
	
	public void register()
	{
		System.out.println("=========Register user=========");
		while(user == null)
		{
			System.out.print("Brukernavn: ");
			String username = input.nextLine();
			System.out.print("Passord: ");
			String password = input.nextLine();
			System.out.print("Navn: ");
			String name = input.nextLine();
			System.out.print("epost: ");
			String email = input.nextLine();
			
			user = sql.user.addUser(username, password, name, email);
			
			if (user == null)
				System.out.println("Prøv igjen");
		}
		mainMenu();
	}
	
	public void mainMenu()
	{
		System.out.println("=========Logged in as " + user.getName() + "=========");
		int valg = 0;
		while(valg <= 0 || valg >= 5)
		{
			System.out.println("1. Vis avtaler");
			System.out.println("1. Lag avtale");
			System.out.println("2. Vis varsler");
			System.out.println("3. Vis alarmer");
			System.out.println("4. Vis mine grupper");
			System.out.println("5. Vis alle grupper");
			System.out.println("6. Lag gruppe");
			System.out.print("> ");
			valg = input.nextInt();
		}
		
		if (valg == 4)
			groupMenu(sql.group.getGroups(user));
		else if (valg == 5)
			groupMenu(sql.group.getAllGroups());
		else if (valg == 6)
			createGroup();
	}

	public void groupMenu(ArrayList<Group> groups)
	{
		System.out.println("=========Viser avtaler=========");
		for (int i = 0; i < groups.size(); i++)
			System.out.println((i+1) + ": " + groups.get(i));

		int valg = 0;
		while(valg >= 0 && valg <= groups.size())
		{
			System.out.println("0. Tilbake");
			if (groups.size() > 0)
				System.out.println(1 + "-" + groups.size() + ": Vis gruppe");
			System.out.print("> ");
			valg = input.nextInt();
		}
	}
	
	public void createGroup()
	{
		Group group = null;

		System.out.print("Navn: ");
		String name = input.nextLine();
			
		group = sql.group.addGroup(name);
	}
}
