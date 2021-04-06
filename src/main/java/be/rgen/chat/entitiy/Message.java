package be.rgen.chat.entitiy;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "message")
public class Message extends PanacheEntity implements Comparable {
    @OneToOne
    public User author;
    public String content;
    public Timestamp time;

    public Message() { }

    public Message(User author, String content, Timestamp time) {
        this.author = author;
        this.content = content;
        this.time = time;
    }

    @Override
    public int compareTo(Object o) {
        return this.time.compareTo(((Message)o).time);
    }

    @Override
    public String toString() {
        return "Message{" +
                "author=" + author +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", id=" + id +
                '}';
    }
}
