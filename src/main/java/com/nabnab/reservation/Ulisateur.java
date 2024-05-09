package com.nabnab.reservation;

public class Ulisateur {
    public String cin;
    public String name;
    public String username;

    public Ulisateur(String cin, String name, String username) {
        this.cin = cin;
        this.name = name;
        this.username = username;
    }
    public Ulisateur() {

    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Ulisateur{" +
                "cin='" + cin + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
