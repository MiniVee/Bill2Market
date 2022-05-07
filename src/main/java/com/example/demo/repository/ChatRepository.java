package com.example.demo.repository;

import com.example.demo.model.chat.Chat;
import com.example.demo.model.chat.ChatAlarmResponseDTO;
import com.example.demo.model.client.Client;
import com.example.demo.model.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    public Optional<Chat> findByLenterAndItem(Client lenter, Item item);

    @Query()
    public ChatAlarmResponseDTO findByClientIndex(Integer clientIndex);
}
