package com.gmq.students.weownfinal.weownfinal.controller;

import com.gmq.students.weownfinal.weownfinal.dto.Message;
import com.gmq.students.weownfinal.weownfinal.entity.User;
import org.apache.commons.lang3.StringUtils;
import com.gmq.students.weownfinal.weownfinal.dto.UserDTO;
import com.gmq.students.weownfinal.weownfinal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = "http://localhost:4200") //Enlace con Angular
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        if(StringUtils.isBlank(userDTO.getEmail())) {
            return new ResponseEntity(new Message("The name is required"), HttpStatus.BAD_REQUEST);
        }
        if(userService.existsByEmail(userDTO.getEmail())) {
            return new ResponseEntity(new Message("The email already exists exists"),HttpStatus.BAD_REQUEST);
        }
        userService.save(new User(userDTO.getEmail(),userDTO.getPassword(),userDTO.getFirstName(),userDTO.getLastName(),userDTO.getDob(),userDTO.getImage()));

        return new ResponseEntity(new Message("Username registered!"),HttpStatus.OK);

    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable("email")String email) {
        if(!userService.existsByEmail(email)) {
            return new ResponseEntity(new Message("Not Exists"),HttpStatus.NOT_FOUND);
        }
        User user = userService.getByEmail(email).get();
        userService.delete(user.getId());

        return new ResponseEntity(new Message("User deleted"),HttpStatus.OK);
    }

}
