package com.wtc.dto.request;
import com.wtc.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageRequest {
    public Long receiverId;
    public Long segmentId;
    @NotBlank public String content;
    public MessageType type = MessageType.TEXT;
    public String deeplink;
}
