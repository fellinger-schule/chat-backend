package be.rgen.chat.dto;

public class MessageSendDTO {
    public long roomId;
    public String content;

    public MessageSendDTO() {}

    public String getContent() {
        return content;
    }

    public long getRoomId() { return roomId; }
}
