package com.gmq.students.weownfinal.weownfinal.dto;

import javax.validation.constraints.NotBlank;

public class ProfileDTO {

    @NotBlank
    private String description;

    private String email;

    public ProfileDTO() {

    }

    public ProfileDTO(String description) {

        this.description = description;
    }

    public ProfileDTO(String description, String email) {
        this.description = description;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
