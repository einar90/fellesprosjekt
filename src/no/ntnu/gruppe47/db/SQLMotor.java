package no.ntnu.gruppe47.db;

import java.io.IOException;
import java.sql.SQLException;

/**
 * The Factory should be the interface where Objects are created and they are mapped to entries in the database
 *
 * @author orestis
 */
public class SQLMotor {

    DBConnection db;

    public SQLMotor() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {
        db = new DBConnection("properties");
        db.closeConnection();
    }

}
