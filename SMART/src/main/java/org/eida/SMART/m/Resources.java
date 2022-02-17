package org.eida.SMART.m;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Resources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type;
    private String title;
    private String description;
    private String url;
    private String addDate;
    private String file;

    @ManyToOne
    private User user;

    @ManyToOne
    private Category category;

    public Resources(String type, String title, String url, String description, String addDate, String file, User user) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.url = url;
        this.addDate = addDate;
        this.file = file;
        this.user = user;
    }
}
