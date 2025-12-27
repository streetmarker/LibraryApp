package com.example.libraryApp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "AppUser")
public class AppUser implements Comparable<AppUser>{

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "year_born")
    private Integer yearBorn;

    public AppUser() {
    }

    public AppUser(String firstName, String lastName, Integer yearBorn) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearBorn = yearBorn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public int compareTo(AppUser o) {
        return Integer.compare(this.yearBorn, o.yearBorn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppUser appUser = (AppUser) o;

        if (id != appUser.id) return false;
        if (!firstName.equals(appUser.firstName)) return false;
        if (!lastName.equals(appUser.lastName)) return false;
        return yearBorn.equals(appUser.yearBorn);
    }
}
