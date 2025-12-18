package com.example.libraryApp;


public class RestRequest {

    public Book book;
    public AppUser appUser;
    public Integer appUserId;
    public Integer bookId;

    public RestRequest(Book book, AppUser appUser, Integer appUserId, Integer bookId) {
        this.book = book;
        this.appUser = appUser;
        this.appUserId = appUserId;
        this.bookId = bookId;
    }

}
