package be.rgen.chat.dto;

public class UserRegisterDTO {
    public String name;
    public String username;
    public String password;

    public UserRegisterDTO() {}

    public UserRegisterDTO(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
}
