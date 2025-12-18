package com.example.libraryApp;

public class RestRequest {

    public Book book;
    public AppUser appUser;

    public RestRequest(Book book, AppUser appUser) {
        this.book = book;
        this.appUser = appUser;
    }

}
