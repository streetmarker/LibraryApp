package com.example.libraryApp;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Borrowing")
public class Borrowing {

    @Id
    @GeneratedValue
    int id;
    int bookId;
    int userId;
    Date borrowDate;
    Date returnDate;
    String status;

    public Borrowing() {

    }

    public Borrowing(int bookId, int userId, Date borrowDate, Date returnDate, String status) {
        this.bookId = bookId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public void setUuid(int id) {
        this.id = id;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public int getUuid() {
        return id;
    }

    @Override
    public String toString() {
        return "Borrowing{" +
                "id=" + id +
                ", book=" + bookId +
                ", user=" + userId +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                ", status=" + status +
                '}';
    }
}
