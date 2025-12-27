package com.example.libraryApp.repository;

import java.util.*;
import org.springframework.stereotype.Repository;

import com.example.libraryApp.model.AppUser;
import com.example.libraryApp.model.Book;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class AppUserRepository {

    @PersistenceContext
    private EntityManager em;

    public AppUser findById(int id) {
        return em.find(AppUser.class, id);
    }

    public void save(AppUser appUser) {
        em.persist(appUser);
    }

    public void saveAll(Collection<AppUser> appUsers) {
        for (AppUser appUser : appUsers) {
            em.persist(appUser);
        }
    }

    public List<AppUser> findAll() {
        return em.createQuery("FROM AppUser", AppUser.class).getResultList();
    }

}
