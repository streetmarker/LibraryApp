package com.example.libraryApp.model;

public class RestRequest {

    private Book book;
    private AppUser appUser;
    private Integer appUserId;
    private Integer bookId;

    public RestRequest(Book book, AppUser appUser, Integer appUserId, Integer bookId) {
        this.book = book;
        this.appUser = appUser;
        this.appUserId = appUserId;
        this.bookId = bookId;
    }


    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Integer getAppUserId() {
        return this.appUserId;
    }

    public void setAppUserId(Integer appUserId) {
        this.appUserId = appUserId;
    }

    public Integer getBookId() {
        return this.bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
    
}
