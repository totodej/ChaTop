package com.project.chatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.chatop.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{

}
