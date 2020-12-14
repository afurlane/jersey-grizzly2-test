package org.example;

import org.apache.logging.log4j.Logger;
import org.example.entities.ExampleEntity;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;


@Path("employee")
public class MyEntityResource {

    @PersistenceUnit(unitName = "example-unit")
    private EntityManagerFactory entityManagerFactory;

    @Inject Logger logger;

    @Path("{id}")
    @GET
    public void GetEntityById(@Suspended final AsyncResponse asyncResponse, @PathParam("id") String id) {
        asyncResponse.setTimeoutHandler(new TimeoutHandler() {

            @Override
            public void handleTimeout(AsyncResponse asyncResponse) {
                asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Operation time out.").build());
            }
        });
        asyncResponse.setTimeout(20, TimeUnit.SECONDS);
        new Thread(new Runnable() {

            @Override
            public void run() {
                Response result = veryExpensiveOperation();
                asyncResponse.resume(result);
            }

            private Response veryExpensiveOperation() {
                EntityManager entityManager = entityManagerFactory.createEntityManager();
                ExampleEntity exampleEntity = entityManager.find(ExampleEntity.class, 1);
                return Response.status(Response.Status.OK).entity(exampleEntity).build();
            }
        }).start();
    }

    @Path("{id}")
    @DELETE
    public Response DeleteEntityById(@PathParam("id") String id) {
        return null;
    }

    @PreDestroy
    public void PreDestroyMyEntityResource()
    {
        entityManagerFactory.close();
    }
}
