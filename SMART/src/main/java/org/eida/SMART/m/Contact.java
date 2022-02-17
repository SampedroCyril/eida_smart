package org.eida.SMART.m;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String object;
    private String text;
    private String contactDate;

    @ManyToOne
    private User user;

    public Contact(String object, String text, String contactDate, User user) {
        this.object = object;
        this.text = text;
        this.contactDate = contactDate;
        this.user = user;
    }
}
