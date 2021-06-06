package com.gmq.students.weownfinal.weownfinal.service;

import com.gmq.students.weownfinal.weownfinal.entity.Profile;
import com.gmq.students.weownfinal.weownfinal.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProfileService {

    @Autowired
    ProfileRepository profileRepository;

    public void save(Profile profile) {
        profileRepository.save(profile);
    }

    public void delete(int id) {
        profileRepository.deleteById(id);
    }
    public boolean existsById(int id) {
        return profileRepository.existsById(id);
    }

}
