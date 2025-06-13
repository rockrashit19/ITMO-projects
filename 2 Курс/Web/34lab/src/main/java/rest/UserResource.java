package rest;

import beans.UserBean;
import entities.User;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
public class UserResource {
    @EJB
    private UserBean userBean;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserCredentials credentials) {
        User user = userBean.authenticate(credentials.getUsername(), credentials.getPassword());
        if (user != null) {
            return Response.ok(user).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
    }

    public static class UserCredentials {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}