package com.gmq.students.weownfinal.weownfinal.controller;

import com.gmq.students.weownfinal.weownfinal.dto.Message;
import com.gmq.students.weownfinal.weownfinal.entity.User;
import com.gmq.students.weownfinal.weownfinal.security.dto.JwtDTO;
import com.gmq.students.weownfinal.weownfinal.security.dto.LoginUserDTO;
import com.gmq.students.weownfinal.weownfinal.security.dto.NewUserDTO;
import com.gmq.students.weownfinal.weownfinal.security.jwt.JwtProvider;
import com.gmq.students.weownfinal.weownfinal.service.UserService;
import com.gmq.students.weownfinal.weownfinal.utils.FileUtils;
import com.gmq.students.weownfinal.weownfinal.utils.ProfileHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200") //Enlace con Angular
public class UserController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/register")
    public ResponseEntity<?> newUser(@Validated @RequestBody NewUserDTO newUserDTO, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return new ResponseEntity(new Message("Campos introducidos incorrectos"), HttpStatus.BAD_REQUEST);
        }
        if(userService.existsByEmail((newUserDTO.getEmail()))) {
            return new ResponseEntity(new Message("Ese email ya está registrado"), HttpStatus.BAD_REQUEST);
        }

        User user = new User(newUserDTO.getEmail(),passwordEncoder.encode(newUserDTO.getPassword()),newUserDTO.getFirstName(), newUserDTO.getLastName(),newUserDTO.getDob(),"","Esta es mi descripcción");

        /* Para añadir la implementación del admin
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolName(RolName.ROLE_USER).get());
        if(newUserDTO.getRoles().contains("admin")) {
            roles.add(rolService.getByRolName(RolName.ROLE-ADMIN).get());
        }
        user.setRoles(roles);
        */
        userService.save(user);
        return new ResponseEntity(new Message("Usuario registrado con éxito"),HttpStatus.CREATED);
    }

    @PostMapping("/changepassword")
    public ResponseEntity<?> changePassword( @RequestBody NewUserDTO newUserDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return new ResponseEntity(new Message("Campos introducidos incorrectos"), HttpStatus.BAD_REQUEST);
        }

        User user = userService.getByEmail(newUserDTO.getEmail()).get();
        user.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));

        userService.save(user);
        return new ResponseEntity(new Message("Contraseña actualizada"),HttpStatus.OK);
    }


    @GetMapping("/user/{email}")
    public ResponseEntity<User> getUser(@PathVariable("email")String email) {
        if(!userService.existsByEmail(email)){
            return new ResponseEntity(new Message("Algo ha salido mal"),HttpStatus.NOT_FOUND);
        }
        User user = userService.getByEmail(email).get();
        return new ResponseEntity<User>(user,HttpStatus.OK);
    }

    @GetMapping("/description/{email}")
    public ResponseEntity<User> getDescription(@PathVariable("email")String email) {

        if(!userService.existsByEmail(email)){
            return new ResponseEntity(new Message("Algo ha salido mal"),HttpStatus.NOT_FOUND);
        }
        User user = userService.getByEmail(email).get();

        return new ResponseEntity<User>(user,HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@Valid @RequestBody LoginUserDTO loginUserDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return new ResponseEntity(new Message("Campos introducidos incorrectos"), HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDTO.getEmail(),loginUserDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        JwtDTO jwtDTO = new JwtDTO(jwt,userDetails.getUsername(),userDetails.getAuthorities());

        return new ResponseEntity(jwtDTO,HttpStatus.OK);



    }

    @PutMapping("/description")
    public ResponseEntity<?> setUserDescription(@RequestBody String[] dual) {

        String email = dual[0];
        String description = dual[1];

        if(!userService.getByEmail(email).isPresent()) {
            return new ResponseEntity(new Message("Algo ha salido mal, inténtelo de nuevo más tarde"),HttpStatus.BAD_REQUEST);
        }
        User user = userService.getByEmail(email).get();
        user.setDescription(description);
        userService.save(user);

        return new ResponseEntity(new Message("Perfil modificado con éxito"),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable("email")String email) {
        if(!userService.existsByEmail(email)) {
            return new ResponseEntity(new Message("El usuario no existe"),HttpStatus.NOT_FOUND);
        }
        User user = userService.getByEmail(email).get();
        userService.delete(user.getId());

        return new ResponseEntity(new Message("Usuario eliminado"),HttpStatus.OK);
    }

    @PostMapping("/profileimage/")
    public ResponseEntity<?> setPathImage(@RequestBody ProfileHelper profileHelper) {


        String email = profileHelper.getEmail();
        MultipartFile file = profileHelper.getFile();

        if (!file.isEmpty()) {
            // Get the file name, including the suffix
            String fileName = email + "jpg";

            // Store in this path: the path is under the static file in the project directory: (Note: this file may need to be created by yourself)
            // The reason for putting it under static is that it stores static file resources, that is, you can enter the local server address through the browser, and you can access it when adding the file name
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/";

            try {
                // This method is a package for writing files. In the util class, import the package and use it. The method will be given later
                FileUtils.fileupload(file.getBytes(), path, fileName);
            } catch (IOException e) {

                e.printStackTrace();
                return new ResponseEntity(new Message("Error"), HttpStatus.BAD_REQUEST);
            }

            User user = userService.getByEmail(email).get();

            // Then create the corresponding entity class, add the following path, and then write through the database operation method
            user.setImage("http://localhost:8080/" + fileName);
            userService.save(user);


            return new ResponseEntity(new Message("Foto Actualizada"), HttpStatus.OK);
        }
        return new ResponseEntity(new Message("Error"), HttpStatus.BAD_REQUEST);

    }
}
