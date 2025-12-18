package com.example.libraryApp;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class BookRepository {

    @PersistenceContext
    private EntityManager em;

    public Book findById(int id) {
        return em.find(Book.class, id);
    }

    public void save(Book book) {
        em.persist(book);
    }

    public void update(Book book) {
        em.merge(book);
    }

    public List<Book> findAll() {
        return em.createQuery("FROM Book", Book.class).getResultList();
    }

    public boolean isBookFree(int bookId) { // todo book class should have isAvailable field
        try {
            Boolean status = em.createQuery("SELECT b.isAvailable FROM Book b WHERE b.id = :id", Boolean.class)
                    .setParameter("id", bookId)
                    .getSingleResult();
            return status;
        } catch (Exception e) {
            return true;
        }
    }
}
