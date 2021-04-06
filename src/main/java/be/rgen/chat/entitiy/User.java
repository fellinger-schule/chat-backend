package be.rgen.chat.entitiy;

import be.rgen.chat.Assembler;
import be.rgen.chat.dto.UserLoginDTO;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Usr")
public class User extends PanacheEntity {
    public String name;
    public String username;
    public String passwordHash;
    @ManyToMany
    @JoinTable(
        name = "user_room",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    public Set<Room> rooms = new HashSet<Room>();

    public User(String name, String username, String passwordHash) {
        this.name = name;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public User() {}

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", id=" + id +
                '}';
    }

    public boolean joinRoom(Room room) {
        Boolean result = rooms.add(room);
        this.persist();
        return result;
    }

    public static User findByUsername(String username) {
       return Assembler.toUser(new UserLoginDTO(username, ""));
    }
}
