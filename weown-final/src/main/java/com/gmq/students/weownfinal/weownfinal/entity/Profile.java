package com.gmq.students.weownfinal.weownfinal.entity;


import javax.persistence.*;

@Entity
@Table(name = "profiles")
public class Profile  {

    @Id
    @Column(name = "profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "description")
    private String description;

    public Profile() {

    }

    public Profile(String description) {

        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

