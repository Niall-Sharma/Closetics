package com.cs309.websocket3.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>{

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM messages", nativeQuery = true)
    void deleteByUserName(String userName);

    @Query(value = "SELECT * FROM messages", nativeQuery = true)
    List<Message> GetMesseges();
}
