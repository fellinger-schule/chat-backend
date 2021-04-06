package be.rgen.chat.resource;

import be.rgen.chat.Assembler;
import be.rgen.chat.dto.RoomOverviewDTO;
import be.rgen.chat.dto.UserInfoDTO;
import be.rgen.chat.dto.UserLoginDTO;
import be.rgen.chat.dto.UserRegisterDTO;
import be.rgen.chat.entitiy.User;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.specimpl.ResponseBuilderImpl;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/user")
@RequestScoped
public class UserResource {
    @Inject
    JsonWebToken jwt;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @POST
    @Transactional
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response.Status register(UserRegisterDTO newUser) {
        Assembler.toUser(newUser).persist();
        return Response.Status.OK;
    }

    @POST
    @Transactional
    @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(UserLoginDTO loginCredentials) {
        Response response;
        var user = User.findByUsername(loginCredentials.username);
        if(user != null) {
            //TODO: Actually use hashes
            if (!user.passwordHash.equals(loginCredentials.password)) {
                //User exist, password does not match
                response = new ResponseBuilderImpl()
                        .status(Response.Status.BAD_REQUEST)
                        .entity("Password incorrect")
                        .build();

            } else {
                //User exists, password matches
                String token =  Jwt.issuer("https://example.com/issuer")
                        .subject(user.id.toString())
                        .preferredUserName(user.username)
                        .sign();

                response = new ResponseBuilderImpl()
                        .status(Response.Status.OK)
                        .entity(token)
                        .build();
            }
        } else {
            //User doesn't exist
            response = new ResponseBuilderImpl()
                    .status(Response.Status.BAD_REQUEST)
                    .entity("User not found")
                    .build();
        }
        return response;
    }

    @GET
    @Path("roomOverview")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Set<RoomOverviewDTO> getRoomOverview(@Context SecurityContext ctx) {
        Set<RoomOverviewDTO> result = new HashSet<RoomOverviewDTO>();
        User user = User.findById(Long.parseLong(jwt.getClaim(Claims.sub)));
        if(user != null) {
            result = user.rooms.parallelStream().map(room -> room.getOverView()).collect(Collectors.toSet());
        }
        return result;
    }

    @GET
    @Path("info")
    @Produces(MediaType.APPLICATION_JSON)
    public UserInfoDTO getInfo(@Context SecurityContext ctx) {
        var user = User.findById(Long.parseLong(jwt.getClaim(Claims.sub)));
        if(user != null) {
            return Assembler.toUserInfoDTO((User) user);
        } else {
            return null;
        }
    }
}