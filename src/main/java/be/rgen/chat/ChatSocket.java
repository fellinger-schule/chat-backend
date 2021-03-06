package be.rgen.chat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLOutput;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;

@ServerEndpoint("/chat/{username}")
@ApplicationScoped
public class ChatSocket implements PropertyChangeListener {

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessions.put(username, session);
        ChatObservable.getInstance().addPropertyChangeListener(this);
        System.out.println("User " + username + " joined");
        //broadcast("User " + username + " joined");
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(username);
        System.out.println(username+" disconnected");
        //broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(username);
        System.out.println("User errored out: "+throwable);
        //broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        //broadcast(">> " + username + ": " + message);
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("Property changed: "+sessions.keySet().toString());
        broadcast(String.format("{\"type\": \"%s\", \"roomId\": %d}", (String)evt.getPropertyName(), (long)evt.getNewValue()));
    }
}
