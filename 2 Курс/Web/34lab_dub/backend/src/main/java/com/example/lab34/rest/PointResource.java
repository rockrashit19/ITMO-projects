package com.example.lab34.rest;

import com.example.lab34.ejb.PointService;
import com.example.lab34.entity.PointResult;
import com.example.lab34.entity.User;

import javax.ejb.EJB; import javax.ws.rs.*; import javax.ws.rs.core.*;

import java.util.List;
@Path("points") @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
public class PointResource {
    @EJB private PointService ps;
    public static class CheckRequest { public double x, y, r; public Long userId; }
    @POST @Path("check") public PointResult check(CheckRequest req) {
        User dummy = new User(); dummy.setId(req.userId);
        return ps.checkPoint(req.x, req.y, req.r, dummy);
    }
    @GET @Path("history/{userId}") public List<PointResult> history(@PathParam("userId") Long id) {
        User dummy = new User(); dummy.setId(id);
        return ps.getHistory(dummy);
    }
}
