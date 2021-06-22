package com.gmq.students.weownfinal.weownfinal.entity;


import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date")
    private Date date;

    @Column(name = "text")
    private String text;

    public MessageEntity() {

    }

    public MessageEntity(Date date, String text) {
        this.date = date;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

}
