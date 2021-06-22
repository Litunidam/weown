package com.gmq.students.weownfinal.weownfinal.service;

import com.gmq.students.weownfinal.weownfinal.entity.MessageEntity;
import com.gmq.students.weownfinal.weownfinal.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    public void save(MessageEntity messageEntity) {
        messageRepository.save(messageEntity);
    }
}
