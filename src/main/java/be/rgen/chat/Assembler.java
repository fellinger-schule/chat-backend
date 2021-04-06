package be.rgen.chat;

import be.rgen.chat.dto.*;
import be.rgen.chat.entitiy.*;

public final class Assembler {
    private Assembler() {};

    public static User toUser(UserRegisterDTO usr) {
        return new User(usr.name, usr.username, usr.password);
    }

    public static User toUser(UserLoginDTO usr) {
        return User.find("username", usr.username).firstResult();
    }

    public static MessageInfoDTO toMessageInfoDTO(Message msg) {
        return new MessageInfoDTO(msg.author.id, msg.author.name,msg.time, msg.content);
    }

    public static UserInfoDTO toUserInfoDTO(User user) {
        return new UserInfoDTO(user.id, user.username, user.name);
    }
}
