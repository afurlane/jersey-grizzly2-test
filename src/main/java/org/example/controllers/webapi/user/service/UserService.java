package org.example.controllers.webapi.user.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.entities.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * Service that provides operations for {@link User}s.
 *
 * @author cassiomolin
 */
@ApplicationScoped
public class UserService {

    @Inject
    private EntityManager em;

    /**
     * Find a user by username or email.
     *
     * @param identifier
     * @return
     */
    public User findByUsernameOrEmail(String identifier) {
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier", User.class)
                .setParameter("identifier", identifier)
                .setMaxResults(1)
                .getResultList();
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    /**
     * Find all users.
     *
     * @return
     */
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    /**
     * Find a user by id.
     *
     * @param userId
     * @return
     */
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(em.find(User.class, userId));
    }
}
