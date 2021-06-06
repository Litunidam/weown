package com.gmq.students.weownfinal.weownfinal.security.entity;

import java.util.HashSet;
import java.util.Set;

public class User {


    private int id;
    private String name;
    private String email;
    private String password;
    private Set<Rol> roles = new HashSet<>();



}
