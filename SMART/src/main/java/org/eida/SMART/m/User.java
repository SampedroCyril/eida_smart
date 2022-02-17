package org.eida.SMART.m;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String adress;
    private int postalCode;
    private String city;
    private String email;
    private String tel;
    private String agencyPlace;
    private String password;
    private int accessLevel;
    private boolean active;
    private String addDate;
    private String disableDate;


    public User(String firstName, String lastName, String adress, int postalCode, String city, String email, String tel, String agencyPlace, String password, int accessLevel, boolean active, String addDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.adress = adress;
        this.postalCode = postalCode;
        this.city = city;
        this.email = email;
        this.tel = tel;
        this.agencyPlace = agencyPlace;
        this.password = password;
        this.accessLevel = accessLevel;
        this.active = active;
        this.addDate = addDate;
    }

    public boolean getActive() {
        return active;
    }
}
