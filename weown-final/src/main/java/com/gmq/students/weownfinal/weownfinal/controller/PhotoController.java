package com.gmq.students.weownfinal.weownfinal.controller;

import com.gmq.students.weownfinal.weownfinal.dto.Message;
import com.gmq.students.weownfinal.weownfinal.dto.PhotoDTO;
import com.gmq.students.weownfinal.weownfinal.entity.Photo;
import com.gmq.students.weownfinal.weownfinal.entity.User;
import com.gmq.students.weownfinal.weownfinal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/photo")
@CrossOrigin(origins = "http://localhost:4200")
public class PhotoController {

    @Autowired
    UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<?> uplaodImage(@RequestParam("imageFile") MultipartFile photo, @RequestParam("title") String title,@RequestParam("desc")String desc, @RequestParam("email")String email) throws IOException {

        Photo img = new Photo(title, photo.getBytes(),desc);
        System.out.println("titulo: "+title+" desc:"+desc);
        if(!userService.existsByEmail((email))) {
            return new ResponseEntity(new Message("Ese email no existe"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.getByEmail(email).get();
        //img.setUser(user);
        List<Photo> photos = user.getPhotos();
        photos.add(img);
        user.setPhotos((ArrayList<Photo>) photos);

        userService.save(user);

        return new ResponseEntity(new Message("Foto subida con éxito"), HttpStatus.CREATED);
    }

}
