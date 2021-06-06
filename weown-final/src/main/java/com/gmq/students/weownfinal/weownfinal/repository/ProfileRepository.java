package com.gmq.students.weownfinal.weownfinal.repository;

import com.gmq.students.weownfinal.weownfinal.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Integer> {

    Optional<Profile> findById(int id);

    boolean existsById(int id);

}
