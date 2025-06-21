package com.example.lab34.rest;

import com.example.lab34.ejb.AuthService;
import com.example.lab34.entity.User;

import javax.ejb.EJB; import javax.ws.rs.*; import javax.ws.rs.core.*;

@Path("auth") @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    @EJB private AuthService auth;
    public static class Creds { public String login; public String passwordHash; }
    @POST @Path("login") public Response login(Creds c, @Context HttpHeaders h) {
        User u = auth.authenticate(c.login, c.passwordHash);
        if(u==null) return Response.status(401).build();

        return Response.ok(u).build();
    }
}
