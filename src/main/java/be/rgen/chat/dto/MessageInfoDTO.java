package be.rgen.chat.dto;

import java.sql.Timestamp;

public class MessageInfoDTO {
    public long authorID;
    public String authorName;
    public String content;
    public Timestamp timestamp;

    public MessageInfoDTO() {}

    public MessageInfoDTO(long authorID, String authorName, Timestamp timestamp, String content) {
        this.authorID = authorID;
        this.authorName = authorName;
        this.timestamp = timestamp;
        this.content = content;
    }
}
