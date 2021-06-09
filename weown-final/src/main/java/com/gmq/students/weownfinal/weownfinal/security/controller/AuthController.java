package com.gmq.students.weownfinal.weownfinal.security.controller;

import com.gmq.students.weownfinal.weownfinal.dto.Message;
import com.gmq.students.weownfinal.weownfinal.entity.User;
import com.gmq.students.weownfinal.weownfinal.security.dto.JwtDTO;
import com.gmq.students.weownfinal.weownfinal.security.dto.LoginUserDTO;
import com.gmq.students.weownfinal.weownfinal.security.dto.NewUserDTO;
import com.gmq.students.weownfinal.weownfinal.security.jwt.JwtProvider;
import com.gmq.students.weownfinal.weownfinal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {


    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/new")
    public ResponseEntity<?> newUser(@Validated @RequestBody NewUserDTO newUserDTO, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return new ResponseEntity(new Message("Campos introducidos incorrectos"), HttpStatus.BAD_REQUEST);
        }
        if(userService.existsByEmail((newUserDTO.getEmail()))) {
            return new ResponseEntity(new Message("Ese email ya está registrado"), HttpStatus.BAD_REQUEST);
        }

        User user = new User(newUserDTO.getEmail(),passwordEncoder.encode(newUserDTO.getPassword()),newUserDTO.getFirstName(), newUserDTO.getLastName(),newUserDTO.getDob(),"");


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


    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@Valid @RequestBody LoginUserDTO loginUserDTO,BindingResult bindingResult) {
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

}
