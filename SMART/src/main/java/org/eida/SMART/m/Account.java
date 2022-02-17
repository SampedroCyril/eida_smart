package org.eida.SMART.m;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double money;
    private String numAccount;
    private String openingDate;
    private String type;
    private boolean active;

    @ManyToOne
    private User user;


    public Account(double money, String numAccount, String openingDate, String type, boolean active, User user) {
        this.money = money;
        this.numAccount = numAccount;
        this.openingDate = openingDate;
        this.type = type;
        this.active = active;
        this.user = user;
    }

    @Override
    public String toString() {
        return "{" +
                "money:" + money +
                ", type:'" + type + '\'' +
                '}';
    }
}
