package org.eida.SMART.m;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int amount;
    private String operationDate;
    private String name;
    private String source;
    private String destination;

    @ManyToOne
    private Account operationAccount;

    public Operation(int amount, String operationDate, String name, String source, String destination, Account operationAccount) {
        this.id = id;
        this.amount = amount;
        this.operationDate = operationDate;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.operationAccount = operationAccount;
    }

    public Operation(int amount, String operationDate, String name, Account operationAccount) {
        this.amount = amount;
        this.operationDate = operationDate;
        this.name = name;
        this.operationAccount = operationAccount;
    }
}

