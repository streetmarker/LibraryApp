package com.example.libraryApp;

import jakarta.persistence.*;

import java.util.Objects;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @GeneratedValue
    int id;

    String title;

    Integer year;
    
    String author;

    @ColumnDefault("true")
    @Column(name = "is_available")
    boolean isAvailable = true;


    public Book() {
    }

    public Book(String title, Integer year, String author) {
        this.title = title;
        this.year = year;
        this.author = author;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(year, book.year) && Objects.equals(author, book.author);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, title, year, author);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", author='" + author + '\'' + 
                ", isAvailable=" + isAvailable +
                '}';
    }
}
