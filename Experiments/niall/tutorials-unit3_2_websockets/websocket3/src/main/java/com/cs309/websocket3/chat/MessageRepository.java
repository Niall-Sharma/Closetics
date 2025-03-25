package com.cs309.websocket3.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, Long>{
    @Query(value = "DELETE * FROM messages WHERE userName = ?")
    public void deleteByUserName(String userName);
}
