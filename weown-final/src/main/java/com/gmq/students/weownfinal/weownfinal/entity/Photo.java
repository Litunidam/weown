package com.gmq.students.weownfinal.weownfinal.entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @Column(name = "photo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "description")
    private String description;

    /*
    //@ElementCollection(fetch=FetchType.EAGER)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
     */


    public Photo() {

    }

    public Photo(String title, byte[] photo, String description) {


        this.title = title;

        this.photo = photo;

        this.description = description;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*
     public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
     */




}
