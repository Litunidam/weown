package com.gmq.students.weownfinal.weownfinal.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class ProfileHelper {
    private String email;
    private MultipartFile file;

    public ProfileHelper(String email, MultipartFile file) {
        this.email = email;
        this.file = file;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
