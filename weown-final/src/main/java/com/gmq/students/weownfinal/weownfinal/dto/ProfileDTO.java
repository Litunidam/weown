package com.gmq.students.weownfinal.weownfinal.dto;

import javax.validation.constraints.NotBlank;

public class ProfileDTO {

    @NotBlank
    private String description;

    public ProfileDTO() {

    }

    public ProfileDTO(String description) {

        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
