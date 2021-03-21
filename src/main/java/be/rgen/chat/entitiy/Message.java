package be.rgen.chat.entitiy;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.sql.Timestamp;

public class Message extends PanacheEntity {
    public User author;
    public String content;
    public Timestamp time;
}
