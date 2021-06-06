package com.gmq.students.weownfinal.weownfinal.dto;

import com.gmq.students.weownfinal.weownfinal.entity.User;

import java.util.List;

public class PhotoDTO {

    private String title;

    private byte[] photo;

    private String description;

    private List<User> users;

    public PhotoDTO() {

    }

    public PhotoDTO(String title, byte[] photo, String description) {


        this.title = title;

        this.photo = photo;

        this.description = description;

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }




}
