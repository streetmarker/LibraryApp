package com.example.libraryApp.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.libraryApp.constans.BorrowingStatus;
import com.example.libraryApp.model.Borrowing;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class BorrowingRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Borrowing borrowing) {
        em.persist(borrowing);
    }
    public Borrowing findByBookId(int id) {
        return em.createQuery("FROM Borrowing b WHERE b.bookId = :id AND b.status = 'BORROWED'", Borrowing.class)
                .setParameter("id", id)
                .getSingleResult();
    }
    public List<Borrowing> findAll() {
        return em.createQuery("FROM Borrowing", Borrowing.class).getResultList();
    }
    public boolean updateBorrowing(int bookId, int userId, Date returnDate, BorrowingStatus status) {
        em.createQuery("UPDATE Borrowing b SET b.returnDate = :returnDate, b.status = :status WHERE b.bookId = :bookId AND b.userId = :userId AND b.status = 'BORROWED'")
          .setParameter("returnDate", returnDate)
          .setParameter("status", status.name())
          .setParameter("bookId", bookId)
          .setParameter("userId", userId)
          .executeUpdate();
        return true;
    }
    public String checkBorrowingStatus(int bookId, int userId) {
        try {
            Borrowing borrowing = em.createQuery("FROM Borrowing b WHERE b.bookId = :bookId AND b.userId = :userId AND b.status = 'BORROWED'", Borrowing.class)
                    .setParameter("bookId", bookId)
                    .setParameter("userId", userId)
                    .getSingleResult();
            if (borrowing != null) {
                return borrowing.getStatus();
            } else {
                return "NOT_BORROWED";
            }
        } catch (Exception e) {
            return "NOT_BORROWED";
        }
    }
    
}
