package be.rgen.chat.dto;

public class UserInfoDTO {
    public long id;
    public String username;
    public String name;

    public UserInfoDTO() {}

    public UserInfoDTO(long id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }
}
