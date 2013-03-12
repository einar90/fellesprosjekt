package no.ntnu.gruppe47;

import java.sql.ResultSet;

import no.ntnu.gruppe47.db.SQLMotor;

public class CalendarSystem {
	public static void main(String[] args) {
		try
		{
			SQLMotor sql = new SQLMotor();
	        ResultSet rs = sql.db.makeSingleQuery("Select * from Person");
	        System.out.println(rs);
	        sql.db.closeConnection();
		}
		catch(Exception e)
		{
			System.out.println(e.getClass().getSimpleName());
			System.out.println(e.getMessage());
		}
	}
}
