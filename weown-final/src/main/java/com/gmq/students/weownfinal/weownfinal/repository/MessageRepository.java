package com.gmq.students.weownfinal.weownfinal.repository;

import com.gmq.students.weownfinal.weownfinal.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity,Integer> {
}
