package be.rgen.chat.entitiy;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Usr")
public class User extends PanacheEntity {
    public String name;
    public String username;
    public String passwordHash;

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
}
