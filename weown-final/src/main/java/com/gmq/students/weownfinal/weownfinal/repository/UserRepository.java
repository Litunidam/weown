package com.gmq.students.weownfinal.weownfinal.repository;

import com.gmq.students.weownfinal.weownfinal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByFirstName(String firstName);

    Optional<User> findByEmail(String email);

    boolean existsById(int id);

    boolean existsByFirstName(String firstName);

    boolean existsByEmail(String email);
}
