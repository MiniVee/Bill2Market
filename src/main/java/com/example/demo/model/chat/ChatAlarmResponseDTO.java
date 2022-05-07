package com.example.demo.model.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChatAlarmResponseDTO {
    private String notReadMessageContent;
    private Integer notReadMessageNum;
    private String nickname;
}
