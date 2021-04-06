package be.rgen.chat.dto;

import java.sql.Timestamp;

public class RoomOverviewDTO {
    public long roomId;
    public String roomName;
    public String lastMessageAuthorName = "";
    public String lastMessageContent = "";
    public Timestamp lastMessageTimestamp = null;

    public RoomOverviewDTO() {
    }

    public RoomOverviewDTO(long roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public RoomOverviewDTO(long roomId, String roomName, String lastMessageAuthorName, String lastMessageContent, Timestamp lastMessageTimestamp) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.lastMessageAuthorName = lastMessageAuthorName;
        this.lastMessageContent = lastMessageContent;
        this.lastMessageTimestamp = lastMessageTimestamp;
    }
}
