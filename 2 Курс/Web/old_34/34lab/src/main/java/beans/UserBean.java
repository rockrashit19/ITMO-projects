package beans;

import entities.User;
import util.PasswordUtil;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserBean {
    @PersistenceContext(unitName = "LabPU")
    private EntityManager em;

    public User authenticate(String username, String password) {
        User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
        if (user != null && user.getPasswordHash().equals(PasswordUtil.hashPassword(password))) {
            return user;
        }
        return null;
    }

    public void register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        em.persist(user);
    }
}