package no.ntnu.gruppe47.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class DBConnection {
	
	private Connection conn;
	
	Properties props = new Properties();
	
	public DBConnection(String propertyFile) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException
	{
		File f = new File(propertyFile);
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		this.props = p;
		initializeDB();
	}
	
	public DBConnection(Properties connectionProperties) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException
	{	
		this.props = connectionProperties;
		initializeDB();
	}
	
	private void initializeDB() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection(props.getProperty("dbAddress"), props);
	}
	
	public ResultSet makeSingleQuery(String query) throws SQLException
	{
		Statement st=conn.createStatement();
		//st.setQueryTimeout(10800);
		ResultSet rs= st.executeQuery(query);
		return rs;
	}
	
	/**
	 * Make single update query like insert or update
	 * 
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public int makeUpdate(String query) throws SQLException
	{
		Statement st=conn.createStatement();
		int res=st.executeUpdate(query);
		return res;
	}
	
	/**
	 * For making batch updates like many insertions.
	 * 
	 * */
	public PreparedStatement preparedStatement(String sql) throws SQLException
	{
		return conn.prepareStatement(sql);
	}
	
	
	public void closeQuery(ResultSet rs) throws SQLException
	{
		if(rs.getStatement()!=null)
			rs.getStatement().close();
		else
			rs.close();
	}
	
	public void closeConnection() throws SQLException
	{
		conn.close();
	}

}
