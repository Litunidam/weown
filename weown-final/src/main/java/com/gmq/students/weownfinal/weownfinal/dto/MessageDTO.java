package com.gmq.students.weownfinal.weownfinal.dto;

import com.gmq.students.weownfinal.weownfinal.entity.User;

import java.sql.Date;

public class MessageDTO {

    private Date date;

    private String text;

    private User userSend;

    private User userReceived;

    public MessageDTO() {

    }

    public MessageDTO(Date date, String text) {
        this.date = date;
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUserSend() {
        return userSend;
    }

    public void setUserSend(User userSend) {
        this.userSend = userSend;
    }

    public User getUserReceived() {
        return userReceived;
    }

    public void setUserReceived(User userReceived) {
        this.userReceived = userReceived;
    }
}
