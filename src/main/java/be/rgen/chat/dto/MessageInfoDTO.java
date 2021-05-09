package be.rgen.chat.dto;

import java.sql.Timestamp;

public class MessageInfoDTO {
    public long authorId;
    public String authorName;
    public String content;
    public Timestamp timestamp;

    public MessageInfoDTO() {}

    public MessageInfoDTO(long authorId, String authorName, Timestamp timestamp, String content) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.timestamp = timestamp;
        this.content = content;
    }
}
