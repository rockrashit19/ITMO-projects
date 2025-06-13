package beans;

import entities.Point;
import entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PointCheckBean {
    @PersistenceContext(unitName = "LabPU")
    private EntityManager em;

    public Point checkPoint(double x, double y, double r, User user) {
        // Проверка области (пример: круг радиуса r)
        boolean isInside = (x * x + y * y <= r * r);

        Point point = new Point();
        point.setX(x);
        point.setY(y);
        point.setR(r);
        point.setInside(isInside);
        point.setTimestamp(LocalDateTime.now());
        point.setUser(user);

        em.persist(point);
        return point;
    }

    public List<Point> getPointsByUser(User user) {
        return em.createQuery("SELECT p FROM Point p WHERE p.user = :user", Point.class)
                .setParameter("user", user)
                .getResultList();
    }
}