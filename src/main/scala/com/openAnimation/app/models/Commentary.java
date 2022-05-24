package com.openAnimation.app.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "commentary")
public class Commentary {
    @Id
    @GeneratedValue
    @Column(nullable = false, length = 36)
    private String id;

    private String comment;
}
