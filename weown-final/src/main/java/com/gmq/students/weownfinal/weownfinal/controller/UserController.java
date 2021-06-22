package com.gmq.students.weownfinal.weownfinal.controller;

import com.gmq.students.weownfinal.weownfinal.dto.Message;
import com.gmq.students.weownfinal.weownfinal.entity.MessageEntity;
import com.gmq.students.weownfinal.weownfinal.entity.Photo;
import com.gmq.students.weownfinal.weownfinal.entity.User;
import com.gmq.students.weownfinal.weownfinal.security.dto.JwtDTO;
import com.gmq.students.weownfinal.weownfinal.security.dto.LoginUserDTO;
import com.gmq.students.weownfinal.weownfinal.security.dto.NewUserDTO;
import com.gmq.students.weownfinal.weownfinal.security.jwt.JwtProvider;
import com.gmq.students.weownfinal.weownfinal.service.MessageService;
import com.gmq.students.weownfinal.weownfinal.service.PhotoService;
import com.gmq.students.weownfinal.weownfinal.service.UserService;
import com.gmq.students.weownfinal.weownfinal.utils.FileUtils;

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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    PhotoService photoService;

    @Autowired
    MessageService messageService;

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

    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable("email")String email) {
        if(!userService.existsByEmail(email)){
            return new ResponseEntity(new Message("Algo ha salido mal"),HttpStatus.NOT_FOUND);
        }
        User user = userService.getByEmail(email).get();

        if(!user.getPhotos().isEmpty()) {
            List<Photo> photos = user.getPhotos();
            Collections.reverse(photos);
            user.setPhotos(photos);
        }

        return new ResponseEntity<User>(user,HttpStatus.OK);
    }

    @GetMapping("/description/{email}")
    public ResponseEntity<String> getDescription(@PathVariable("email")String email) {

        if(!userService.existsByEmail(email)){
            return new ResponseEntity(new Message("Algo ha salido mal"),HttpStatus.NOT_FOUND);
        }
        User user = userService.getByEmail(email).get();
        String desc = user.getDescription();

        return new ResponseEntity<String>(desc,HttpStatus.OK);

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
    public ResponseEntity<?> setUserDescription(@RequestParam("email") String email,@RequestParam("description") String description) {

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
    public ResponseEntity<?> setPathImage(@RequestParam("email") String email, @RequestParam("file") MultipartFile multiFile) throws IOException {

        System.out.println("esto es el email: "+email);

        MultipartFile multipartFile = multiFile;

        System.out.println("nombre del multipart: "+multipartFile.getOriginalFilename());

        if (!multipartFile.isEmpty()) {
            String fileName = email + ".jpg";
            //Ruta para guardar las fotos en el proyecto de angular
            String path = "C:/TFG/weown/front/weownFront/src/assets/";
            try {
                FileUtils.fileupload(multipartFile.getBytes(), path, fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity(new Message("Error"), HttpStatus.BAD_REQUEST);
            }
            User user = userService.getByEmail(email).get();
            user.setImage("./assets/" + fileName);

            userService.save(user);

            return new ResponseEntity(new Message("Foto Actualizada"), HttpStatus.OK);
        }
        return new ResponseEntity(new Message("Error"), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/photo/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("imageFile") MultipartFile photo, @RequestParam("title") String title,@RequestParam("desc")String desc, @RequestParam("email")String email) throws IOException {

        Photo img = new Photo(title, photo.getBytes(),desc);
        System.out.println("titulo: "+title+" desc:"+desc);
        if(!userService.existsByEmail((email))) {
            return new ResponseEntity(new Message("Ese email no existe"), HttpStatus.BAD_REQUEST);
        }

        User user = userService.getByEmail(email).get();

        List<Photo> photos = user.getPhotos();
        photos.add(img);

        user.setPhotos(photos);

        userService.save(user);

        return new ResponseEntity(new Message("Foto subida con éxito"), HttpStatus.CREATED);
    }

    @GetMapping("/photos/{email}")
    public ResponseEntity<List<Photo>> getImages(@PathVariable("email")String email) {

        User user = userService.getByEmail(email).get();

        List<Photo> list = user.getPhotos();

        return new ResponseEntity<List<Photo>>(list,HttpStatus.OK);
    }

    @GetMapping("path/{email}")
    public ResponseEntity<String> getImagePath(@PathVariable("email")String email) {

        User user = userService.getByEmail(email).get();

        String path = user.getImage();

        return new ResponseEntity<String>(path,HttpStatus.OK);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestParam("text")String text,@RequestParam("userEmail")String userEmail,@RequestParam("receiverEmail")String receiverEmail) {

        //recibimos el usuario
        User user = userService.getByEmail(userEmail).get();

        //comprobamos si existe el usuario que recibe el mensaje
        if(!userService.getByEmail(receiverEmail).isPresent()) {
            return new ResponseEntity(new Message("Usuario no encontrado"), HttpStatus.BAD_REQUEST);
        }
        User userReceived = userService.getByEmail(receiverEmail).get();

        Date date = new java.sql.Date(Date.from(Instant.now()).getTime());
        //creamos la entidad mensaje
        MessageEntity messageEntity = new MessageEntity(date,userEmail+" envía: "+text);

        //establecemos los mensajes enviados del usuario que envía
        List<MessageEntity> messagesSends = user.getSends();
        messagesSends.add(messageEntity);
        user.setSends(messagesSends);

        //establecemos los mensajes recibidos del usuario que recibe
        List<MessageEntity> messagesReceived = userReceived.getReceived();
        messagesReceived.add(messageEntity);
        userReceived.setReceived(messagesReceived);

        userService.save(user);
        userService.save(userReceived);

        return new ResponseEntity(new Message("Mensaje enviado"), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getUsers() {

        List<User> users = userService.getUsers();

        return new ResponseEntity<List<User>>(users,HttpStatus.OK);
    }

    @PutMapping("/follow")
    public ResponseEntity<?> followAction(@RequestParam("userEmail")String userEmail,@RequestParam("followUserEmail")String followUserEmail) {

        //obtenemos los usuarios
        User emailUser = userService.getByEmail(userEmail).get();
        User followUser = userService.getByEmail(followUserEmail).get();

        //añadimos a los seguidores al seguido
        if(!followUser.getFollowers().contains(emailUser)){
            followUser.getFollowers().add(emailUser);
        }

        //añadimos los seguidos a nuestro usuario
        if(!emailUser.getFollow().contains(followUser)){
            emailUser.getFollow().add(followUser);
        }

        userService.save(followUser);
        userService.save(emailUser);

        return new ResponseEntity(new Message("Usuarios actualizados"), HttpStatus.OK);
    }

    @GetMapping("/amount/{email}")
    public ResponseEntity<ArrayList<Integer>> followAction(@PathVariable("email")String email) {

        User user = userService.getByEmail(email).get();

        ArrayList<Integer> amount = new ArrayList<>();

        if (user.getFollowers().isEmpty()){
            amount.add(0);
        }else {
            amount.add(user.getFollowers().size());
        }

        if (user.getFollow().isEmpty()){
            amount.add(0);
        } else {
            amount.add(user.getFollow().size());
        }

        return new ResponseEntity<ArrayList<Integer>>(amount, HttpStatus.OK);
    }

}
