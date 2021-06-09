package com.gmq.students.weownfinal.weownfinal.security.entity;

import com.gmq.students.weownfinal.weownfinal.security.enums.RolName;

import javax.persistence.*;

@Entity
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    @Enumerated(EnumType.STRING)
    private RolName rolName;

    public Rol() {

    }
    public Rol(String rol) {
        this.rolName=RolName.ROLE_USER;
    }
    //public Rol(){
    //    this.rolName = RolName.ROLE_USER;
    //}
    public RolName getRolName() {
        return rolName;
    }

    public void setRolName(RolName rolName) {
        this.rolName = rolName;
    }
}
