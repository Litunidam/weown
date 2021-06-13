package com.gmq.students.weownfinal.weownfinal.controller;

import com.gmq.students.weownfinal.weownfinal.dto.Message;
import com.gmq.students.weownfinal.weownfinal.entity.User;
import com.gmq.students.weownfinal.weownfinal.security.dto.JwtDTO;
import com.gmq.students.weownfinal.weownfinal.security.dto.LoginUserDTO;
import com.gmq.students.weownfinal.weownfinal.security.dto.NewUserDTO;
import com.gmq.students.weownfinal.weownfinal.security.jwt.JwtProvider;
import org.apache.commons.lang3.StringUtils;
import com.gmq.students.weownfinal.weownfinal.dto.UserDTO;
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

    /**
    //@PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        
        if(StringUtils.isBlank(userDTO.getFirstName())) {
            return new ResponseEntity(new Message("Introduce un nombre"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(userDTO.getEmail())) {
            return new ResponseEntity(new Message("Introduce un email"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(userDTO.getLastName())) {
            return new ResponseEntity(new Message("Introduce un apellido"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(userDTO.getPassword())) {
            return new ResponseEntity(new Message("Introduce una contraseña"), HttpStatus.BAD_REQUEST);
        }
        
        if(userService.existsByEmail(userDTO.getEmail())) {
            return new ResponseEntity(new Message("El email ya está registrado"),HttpStatus.BAD_REQUEST);
        }
        
        userService.save(new User(userDTO.getEmail(),userDTO.getPassword(),userDTO.getFirstName(),userDTO.getLastName(),userDTO.getDob(),userDTO.getImage()));

        return new ResponseEntity(new Message("¡Usuario registrado!"),HttpStatus.OK);

    }

    //@GetMapping("/login/{email}/{password}")
    public ResponseEntity<User> getUser(@PathVariable("email")String email, @PathVariable("password") String password) {
    	
    	if(!userService.existsByEmail(email)) {
    		return new ResponseEntity(new Message("El usuario no está registrado"),HttpStatus.NOT_FOUND);
    	}
    	
    	User user = userService.getByEmail(email).get();
    	System.out.println(email+" "+password);
    	if(user.getPassword().equals(password)) {
    		return new ResponseEntity<User>(user,HttpStatus.OK);
    	}
    	
    	return new ResponseEntity(new Message("Las credenciales no coinciden"),HttpStatus.BAD_REQUEST);
    	
    	
    }
     */
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

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable("email")String email) {
        if(!userService.existsByEmail(email)) {
            return new ResponseEntity(new Message("El usuario no existe"),HttpStatus.NOT_FOUND);
        }
        User user = userService.getByEmail(email).get();
        userService.delete(user.getId());

        return new ResponseEntity(new Message("Usuario eliminado"),HttpStatus.OK);
    }

}
