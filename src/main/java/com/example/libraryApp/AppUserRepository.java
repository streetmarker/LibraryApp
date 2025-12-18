package com.example.libraryApp;

import java.util.*;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class AppUserRepository {

    @PersistenceContext
    private EntityManager em;

    void save(AppUser appUser) {
        em.persist(appUser);
    }

    void saveAll(Collection<AppUser> appUsers) {
        for (AppUser appUser : appUsers) {
            em.persist(appUser);
        }
    }

    void listAll() {
        List<AppUser> appUsers = em.createQuery("FROM AppUser", AppUser.class).getResultList();
        for (AppUser appUser : appUsers) {
            System.out.println(appUser);
        }
    }

}
