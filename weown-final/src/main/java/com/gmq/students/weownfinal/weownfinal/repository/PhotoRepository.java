package com.gmq.students.weownfinal.weownfinal.repository;

import com.gmq.students.weownfinal.weownfinal.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository  extends JpaRepository<Photo,Integer> {

    Optional<Photo> findById(int id);



}
