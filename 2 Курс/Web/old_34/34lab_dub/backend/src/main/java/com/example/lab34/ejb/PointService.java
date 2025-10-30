package com.example.lab34.ejb;

import com.example.lab34.entity.*;

import javax.ejb.Stateless;
import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
@Stateless
public class PointService {
    @PersistenceContext private EntityManager em;
    public PointResult checkPoint(double x, double y, double r, User user) {
        boolean hit = (x<=0 && y>=0 && x*x+y*y<=r*r/4)
                || (x>=0 && y>=0 && y<=r && x<=r)
                || (x<=0 && y<=0 && x>=-r && y>=-r);
        PointResult pr = new PointResult();
        pr.setX(x); pr.setY(y); pr.setR(r);
        pr.setHit(hit); pr.setTimestamp(LocalDateTime.now()); pr.setUser(user);
        em.persist(pr);
        return pr;
    }
    public List<PointResult> getHistory(User user) {
        return em.createQuery("SELECT p FROM PointResult p WHERE p.user=:u ORDER BY p.timestamp DESC", PointResult.class)
                .setParameter("u", user).getResultList();
    }
}
