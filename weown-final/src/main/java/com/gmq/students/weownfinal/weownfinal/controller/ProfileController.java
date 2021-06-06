package com.gmq.students.weownfinal.weownfinal.controller;


import com.gmq.students.weownfinal.weownfinal.dto.Message;
import com.gmq.students.weownfinal.weownfinal.dto.ProfileDTO;
import com.gmq.students.weownfinal.weownfinal.entity.Profile;
import com.gmq.students.weownfinal.weownfinal.entity.User;
import com.gmq.students.weownfinal.weownfinal.service.ProfileService;
import com.gmq.students.weownfinal.weownfinal.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @PostMapping("/setprofile")
    public ResponseEntity<?> createProfile(@RequestBody ProfileDTO profileDTO) {

        if(StringUtils.isBlank(profileDTO.getDescription())) {
            return new ResponseEntity(new Message("Need description"), HttpStatus.BAD_REQUEST);
        }

        profileService.save(new Profile(profileDTO.getDescription()));

        return new ResponseEntity(new Message("Profile created!"),HttpStatus.OK);

    }

    @GetMapping("/{email}")
    public ResponseEntity<Profile> getProfile(@PathVariable("email") String email){

        if(!userService.existsByEmail(email)) {
            return new ResponseEntity(new Message("Need description"), HttpStatus.NOT_FOUND);
        }
        User user = userService.getByEmail(email).get();

        return new ResponseEntity<Profile>(user.getProfile(),HttpStatus.OK);
    }


}
