package be.rgen.chat.resource;

import be.rgen.chat.Assembler;
import be.rgen.chat.dto.UserLoginDTO;
import be.rgen.chat.dto.UserRegisterDTO;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/User")
public class UserResource {
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response.Status login(UserLoginDTO user) {
        Assembler.toUser(user).persist();
        return Response.Status.OK;
    }

    @GET
    @Path("/info/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getInfo(@PathParam("username") String username) {
        var user = Assembler.toUser(new UserLoginDTO(username, ""));
        if(user != null) {
            return user.toString();
        } else {
            return Response.Status.NOT_FOUND.toString();
        }

    }
}