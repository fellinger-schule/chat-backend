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
}
