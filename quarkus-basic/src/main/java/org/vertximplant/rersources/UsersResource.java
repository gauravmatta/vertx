package org.vertximplant.rersources;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertximplant.entity.Users;


import java.net.URI;
import java.util.List;

@Path("/users")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class UsersResource {

    private static final Logger LOG = LoggerFactory.getLogger(UsersResource.class);

    @GET
    public Uni<List<Users>> get() {
        LOG.info("Get all users...");
        return Users.listAll(Sort.by("id"));
    }

    @GET
    @Path("/{id}")
    public Uni<Users> getById(Long id){
        LOG.info("Get By id: {}",id);
        return Users.findById(id);
    }

    @POST
    public Uni<Response> create(Users user){
        LOG.info("Create: {}",user);
        return Panache.<Users>withTransaction(user::persist)
                .onItem().transform(insertedUser ->
                        Response.created(URI.create("/users"+insertedUser.id)).build()
                );
    }
}
