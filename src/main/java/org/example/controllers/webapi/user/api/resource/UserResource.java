package org.example.controllers.webapi.user.api.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.example.controllers.webapi.user.api.model.QueryUserResult;
import org.example.entities.User;
import org.example.controllers.webapi.user.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JAX-RS resource class that provides operations for users.
 *
 * @author cassiomolin
 */
@RequestScoped
@Path("users")
public class UserResource {

    @Context
    private UriInfo uriInfo;

    @Context
    private SecurityContext securityContext;

    @Inject
    private UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response getUsers() {

        List<QueryUserResult> queryUserResults = userService.findAll().stream()
                .map(this::toQueryUserResult)
                .collect(Collectors.toList());

        return Response.ok(queryUserResults).build();
    }

    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response getUser(@PathParam("userId") Long userId) {

        User user = userService.findById(userId).orElseThrow(NotFoundException::new);
        QueryUserResult queryUserResult = toQueryUserResult(user);
        return Response.ok(queryUserResult).build();
    }

    @GET
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getAuthenticatedUser() {

        Principal principal = securityContext.getUserPrincipal();

        if (principal == null) {
            QueryUserResult queryUserResult = new QueryUserResult();
            queryUserResult.setUsername("anonymous");
            // queryUserResult.setAuthorities(new HashSet<>());
            return Response.ok(queryUserResult).build();
        }

        User user = userService.findByUsernameOrEmail(principal.getName());
        QueryUserResult queryUserResult = toQueryUserResult(user);
        return Response.ok(queryUserResult).build();
    }

    /**
     * Maps a {@link User} instance to a {@link QueryUserResult} instance.
     *
     * @param user
     * @return
     */
    private QueryUserResult toQueryUserResult(User user) {
        QueryUserResult queryUserResult = new QueryUserResult();
        queryUserResult.setId(user.getId());
        queryUserResult.setFirstName(user.getFirstName());
        queryUserResult.setLastName(user.getLastName());
        queryUserResult.setEmail(user.getEmail());
        queryUserResult.setUsername(user.getUsername());
        // queryUserResult.setAuthorities(user.getAuthorities());
        queryUserResult.setActive(user.isActive());
        return queryUserResult;
    }
}