package org.vertximplant;

import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertximplant.entity.Users;


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
}
