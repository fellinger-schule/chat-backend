package be.rgen.chat.entitiy;


import be.rgen.chat.dto.RoomOverviewDTO;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.collection.internal.PersistentSet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "room")
public class Room extends PanacheEntity {
    public String name;
    @ManyToMany(mappedBy = "rooms")
    public Set<User> users;

    @OneToMany (
        targetEntity = Message.class,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @ElementCollection
    public List<Message> messages = null;

    public Room() {}

    public Room(String name) { this.name = name; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getLastNMessages(int n) {
        return messages
                .parallelStream()
                .sorted()
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<Message> getMessagesSince(Timestamp time) {
        return messages
                .parallelStream()
                .filter((msg) -> msg.time.after(time))
                .sorted()
                .collect(Collectors.toList());
    }

    public boolean addMessage(Message message) {
        return messages.add(message);
    }

    public Set<String> getUsernames() {
        return users.parallelStream().map(user -> user.username).collect(Collectors.toSet());
    }

    public Set<Long> getUserIDs() {
        return users.parallelStream().map(user -> (Long)user.id).collect(Collectors.toSet());
    }

    public RoomOverviewDTO getOverView() {
        RoomOverviewDTO result = null;
        if(messages.size() > 0) {
            try {
                Message lastMessage = getLastNMessages(1).get(0);
                result = new RoomOverviewDTO(this.id, this.name, lastMessage.author.name, lastMessage.content, lastMessage.time);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            result = new RoomOverviewDTO(this.id, this.name);
        }
        return result;
    }
}
