package com.example.demo.service.chat;

import com.example.demo.model.chat.ChatAlarmResponseDTO;
import com.example.demo.model.chat.ChatMessageEvent;
import com.example.demo.model.chat.ChatResponseDTO;

public interface ChatService {

    public ChatResponseDTO findChat(Integer itemId, Integer lenterIndex);
//    public void setNonReadCount();
    public void messageSave(ChatMessageEvent chatMessageEvent);
    public ChatAlarmResponseDTO chatAlarmList(Integer clientIndex);

}
