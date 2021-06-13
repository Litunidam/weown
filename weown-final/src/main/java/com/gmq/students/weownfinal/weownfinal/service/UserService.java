package com.gmq.students.weownfinal.weownfinal.service;

import com.gmq.students.weownfinal.weownfinal.entity.User;
import com.gmq.students.weownfinal.weownfinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }


    public Optional<User> getUser(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> getByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public void save(User user) {
        userRepository.save(user);
    }


    public void delete(int id) {
        userRepository.deleteById(id);
    }


    public boolean existsById(int id) {
        return userRepository.existsById(id);
    }

    public boolean existsByFirstName(String firstName) { return userRepository.existsByFirstName(firstName);}

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
