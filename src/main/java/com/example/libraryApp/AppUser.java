package com.example.libraryApp;

import jakarta.persistence.*;

@Entity
@Table(name = "AppUser")
public class AppUser implements Comparable<AppUser>{

    @Id
    @GeneratedValue
    int id;
    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;
    @Column(name = "year_born")
    Integer yearBorn;

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
