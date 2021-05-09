package be.rgen.chat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;

public class ChatObservable {
    private static ChatObservable instance;
    private PropertyChangeSupport support;
    private long changedRoomId;
    private long changedRoomAssociationUserID;

    private ChatObservable() {
        support = new PropertyChangeSupport(this);
    };

    public static synchronized ChatObservable getInstance() {
        if(ChatObservable.instance == null) {
            ChatObservable.instance = new ChatObservable();
        }
        return ChatObservable.instance;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        if(!Arrays.stream(support.getPropertyChangeListeners()).anyMatch(p -> p==pcl)) {
            support.addPropertyChangeListener(pcl);
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void setRoomChanged(long changedRoomId) {
        support.firePropertyChange("roomChanged", -1, changedRoomId);
    }

    public void setRoomAssociationChanged(long userid) {
        support.firePropertyChange("roomAssociationChanged",-1, userid);
    }
}
