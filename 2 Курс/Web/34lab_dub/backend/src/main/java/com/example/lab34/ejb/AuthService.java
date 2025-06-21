package com.example.lab34.ejb;

import javax.ejb.Stateless;
import javax.persistence.*;
import com.example.lab34.entity.User;

@Stateless
public class AuthService {
    @PersistenceContext private EntityManager em;
    public User authenticate(String login, String hash) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.login=:login AND u.passwordHash=:hash", User.class)
                    .setParameter("login", login)
                    .setParameter("hash", hash)
                    .getSingleResult();
        } catch(NoResultException ex) {
            return null;
        }
    }
}
