package be.rgen.chat.resource;

import be.rgen.chat.Assembler;
import be.rgen.chat.dto.MessageInfoDTO;
import be.rgen.chat.dto.MessageSendDTO;
import be.rgen.chat.dto.RoomAddDTO;
import be.rgen.chat.entitiy.Message;
import be.rgen.chat.entitiy.Room;
import be.rgen.chat.entitiy.User;;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/room")
@RequestScoped
public class RoomResource {
    @Inject
    JsonWebToken jwt;

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("addRoom")
    public Response.Status addRoom(@Context SecurityContext ctx, RoomAddDTO roomInfo) {
        boolean success;
        //Automatically add user who created the room to it
        Room room = new Room(roomInfo.getName());
        User user = User.findById(Long.parseLong(jwt.getClaim(Claims.sub)));
        success = user.joinRoom(room);
        room.persist();
        return (success ? Response.Status.OK : Response.Status.BAD_REQUEST);
    }

    @POST
    @Transactional
    @Path("sendMessage")
    public Response.Status sendMessage(@Context SecurityContext ctx, MessageSendDTO msg) {
        boolean success = false;
        boolean msgIsValid = (msg != null && msg.getContent() != null && !msg.getContent().equals(""));
        Room room = Room.findById(msg.getRoomId());
        User user = User.findById(Long.parseLong(jwt.getClaim(Claims.sub)));
        if(room != null && user != null && msgIsValid) {
            success = room.addMessage(new Message(user, msg.getContent(), Timestamp.from(Instant.now())));
        }
        return (success ? Response.Status.OK : Response.Status.BAD_REQUEST);
    }

    @GET
    @Path("info/{roomId}/getUsernames")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getUsernames(@Context SecurityContext ctx, @PathParam("roomId") String roomId) {
        Set<String> result = new HashSet<>();
        Room room = Room.findById(Long.parseLong(roomId));
        if(room != null) {
            result = room.getUsernames();
        }
        return result;
    }

    @GET
    @Path("getMessages/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageInfoDTO> getMessages(@Context SecurityContext ctx, @PathParam("roomId") String roomId) {
        List<MessageInfoDTO> result = new ArrayList<>();
        Room room = Room.findById(Long.parseLong(roomId));
        if(room != null) {
            result = room.getLastNMessages(50)
                    .parallelStream()
                    .map(msg -> Assembler.toMessageInfoDTO(msg))
                    .collect(Collectors.toList());
        }
        return result;
    }

    @GET
    @Path("getMessages/{roomId}/since/{timestamp}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageInfoDTO> getMessages(
            @Context SecurityContext ctx,
            @PathParam("roomId") String roomId,
            @PathParam("timestamp") String since
    ) {
        List<MessageInfoDTO> result = new ArrayList<>();
        Room room = Room.findById(Long.parseLong(roomId));
        if(room != null) {
            Timestamp t = new Timestamp(Date.valueOf(since).getTime());
            result = room.getMessagesSince(t).parallelStream().map(msg -> Assembler.toMessageInfoDTO(msg)).collect(Collectors.toList());
        }
        return result;
    }

    @GET
    @Transactional
    @Path("addUser/{username}/toRoom/{roomId}")
    public Response.Status addUser(
            @Context SecurityContext ctx,
            @PathParam("username") String username,
            @PathParam("roomId") String roomId
    ) {
        Response.Status response = Response.Status.BAD_REQUEST;
        User user = User.findByUsername(username);
        Room room = Room.findById(Long.parseLong(roomId));
        if(user != null && room != null) {
            response = user.joinRoom(room) ? Response.Status.OK : Response.Status.BAD_REQUEST;
        }
        return response;
    }

}
