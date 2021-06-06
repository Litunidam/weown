package com.gmq.students.weownfinal.weownfinal.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private Date dob;

    @Column(name = "image")
    private String image;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Profile profile;


    @ElementCollection(fetch=FetchType.EAGER)
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "users_photos",
            joinColumns = {
                    @JoinColumn(name = "user_id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "photo_id")}
    )
    private List<Photo> photos;

    @ElementCollection(fetch=FetchType.EAGER)
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "users_friends",
            joinColumns = {
                    @JoinColumn(name = "id_follower")},
            inverseJoinColumns = {
                    @JoinColumn(name = "id_follow")}
    )
    private List<User> followers;

    @ElementCollection(fetch=FetchType.EAGER)
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "users_friends",
            joinColumns = {
                    @JoinColumn(name = "id_follow")},
            inverseJoinColumns = {
                    @JoinColumn(name = "id_follower")}
    )
    private List<User> follow;

    @ElementCollection(fetch=FetchType.EAGER)
    @OneToMany(mappedBy="userSend",cascade=CascadeType.ALL)
    private List<MessageEntity> sends;

    @ElementCollection(fetch=FetchType.EAGER)
    @OneToMany(mappedBy="userReceived",cascade=CascadeType.ALL)
    private List<MessageEntity> received;

    public User() {

    }

    public User(String email, String password, String firstName, String lastName, Date dob, String image) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<User> getFollow() {
        return follow;
    }

    public void setFollow(List<User> follow) {
        this.follow = follow;
    }

    public List<MessageEntity> getSends() {
        return sends;
    }

    public void setSends(List<MessageEntity> sends) {
        this.sends = sends;
    }

    public List<MessageEntity> getReceived() {
        return received;
    }

    public void setReceived(List<MessageEntity> received) {
        this.received = received;
    }
}