package at.tgm.ablk.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public static final Logger LOG = LogManager.getLogger("at.tgm.ablk.database.Database");


    private static Database instance;

    public static Database getInstance() {

        if(instance == null) instance = new Database();
        return instance;
    }


    private Connection connection;

    public Database() {

        LOG.debug("Initializing");

        try {

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:user.db");
            connection.setAutoCommit(false);
        } catch(Exception ex) {

            LOG.fatal("Error while connecting to Database", ex);
            System.exit(1);
        }

        LOG.info("Opened database connection successfully");

        try(Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("CREATE TABLE web_user(\n" +
                                              "    email        VARCHAR(128) PRIMARY KEY,\n" +
                                              "    password     VARCHAR(128)\n" +
                                              ");")) {
            LOG.debug("Initialized database table");

            connection.commit();
        } catch(SQLException ex) {
            LOG.fatal("Error creating user table", ex);
            System.exit(1);
        }

    }

    public Connection getConnection() {
        return this.connection;
    }
}
