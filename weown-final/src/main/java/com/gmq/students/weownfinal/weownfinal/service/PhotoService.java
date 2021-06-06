package com.gmq.students.weownfinal.weownfinal.service;

import com.gmq.students.weownfinal.weownfinal.entity.Photo;
import com.gmq.students.weownfinal.weownfinal.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PhotoService {

    @Autowired
    PhotoRepository photoRepository;

    public void save(Photo photo) {
        photoRepository.save(photo);
    }

    public void delete(int id) {
        photoRepository.deleteById(id);
    }

}
