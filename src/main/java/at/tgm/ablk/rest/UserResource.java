package at.tgm.ablk.rest;

import at.tgm.ablk.database.Database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Path("user")
public class UserResource {

    private static final Logger LOG = LogManager.getLogger("at.tgm.klabl.rest.UserResource");


    private Database database;
    public UserResource() {

        LOG.debug("Initializing");
        database = Database.getInstance();
    }

    @POST
    @Consumes({"application/json"})
    @Path("/register")
    public Response register(final User user) {

        if(user.email == null || user.password == null)
            return Response.status(401).header("message", "Invalid registration data").build();

        try(Statement stmt = database.getConnection().createStatement();
            ResultSet res = stmt.executeQuery(String.format("INSERT INTO web_user (email, password)" +
                                                            "VALUES (%s, %s)", user.email, user.password))) {

            database.getConnection().commit();
            return Response.status(201).header("message", "Successfully registered user").build();

        } catch(SQLException ex) {
            LOG.error("Database error while registering", ex);
            return Response.status(500).header("message", "SQL Error, check server log").build();

        } catch(Exception ex) {
            LOG.error("Error while registering", ex);
            return Response.status(500).header("message", "Exception, check server log").build();
        }
    }

    @POST
    @Consumes({"application/json"})
    @Path("/login")
    public Response login(final User user) {

        if(user.email == null || user.password == null)
            return Response.status(403).header("message", "Invalid credentials").build();

        try(Statement stmt = database.getConnection().createStatement();
            ResultSet res = stmt.executeQuery(String.format("SELECT email, password FROM web_user " +
                                                            "WHERE email = %s", user.email))) {

            while(res.next()) {

                if(res.getString("password").equals(user.password))
                    return Response.status(200).header("message", "Successfully logged in").build();
            }

            // not registered
            return Response.status(403).header("message", "Invalid credentials").build();

        } catch(SQLException ex) {
            LOG.error("Database error while logging in", ex);
            return Response.status(500).header("message", "SQL Error, check server log").build();

        } catch(Exception ex) {
            LOG.error("Error while logging in", ex);
            return Response.status(500).header("message", "Exception, check server log").build();
        }
    }
}