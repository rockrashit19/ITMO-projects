package rest;

import beans.PointCheckBean;
import entities.Point;
import entities.User;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/points")
public class PointResource {
    @EJB
    private PointCheckBean pointCheckBean;

    @PersistenceContext(unitName = "LabPU")
    private EntityManager em;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkPoint(PointRequest request) {
        User user = em.find(User.class, request.getUserId());
        if (user == null) {
            Point point = pointCheckBean.checkPoint(request.getX(), request.getY(), request.getR(), user);
            return Response.ok(point).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Point> getPoints(@PathParam("userId") Long userId) {
        User user = em.find(User.class, userId);
        return pointCheckBean.getPointsByUser(user);
    }

    public static class PointRequest {
        private Long userId;
        private double x;
        private double y;
        private double r;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public double getX() { return x; }
        public void setX(double x) { this.x = x; }
        public double getY() { return y; }
        public void setY(double y) { this.y = y; }
        public double getR() { return r; }
        public void setR(double r) { this.r = r; }
    }
}