package org.example;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.Logger;
import org.example.entities.ExampleEntity;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Path(MyEntityResource.MyEntityResourcePath)
public class MyEntityResource {

    final static String MyEntityResourcePath = "myresourceentity";
    final static String MyEntityResourcePathId = "{id}";

    @PersistenceUnit(unitName = "example-unit")
    private EntityManagerFactory entityManagerFactory;

    @Inject Logger logger;

    @Path(MyEntityResourcePathId)
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Operation(summary = "Get ExampleEntity by Id",
            tags = {"Id UUID"},
            description = "Return the example Entity and connected entities",
            responses = {
                    @ApiResponse(description = "The ExampleEntity", content = @Content(
                            schema = @Schema(implementation = ExampleEntity.class)
                    )),
                    @ApiResponse(responseCode = "400", description = "No Id supplied"),
                    @ApiResponse(responseCode = "404", description = "ExampleEntity not found")
            })
    public void GetEntityById(@Suspended final AsyncResponse asyncResponse,
                              @Parameter(
                                      description = "Id of ExampleEntity",
                                      schema = @Schema(
                                                      type = "UUID",
                                                      format = "String",
                                                      description = "Id to be searched"),
                                      required = true)
                              @NotEmpty(message ="Id cannot be null") @PathParam("id") UUID id) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Operation time out.").build()));
        asyncResponse.setTimeout(20, TimeUnit.SECONDS);
        new Thread(() -> {
            entityManagerFactory = javax.persistence.Persistence.createEntityManagerFactory("example-unit");
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            ExampleEntity exampleEntity = entityManager.find(ExampleEntity.class, id);
            entityManager.close();
            Response response;
            if (exampleEntity == null) {
                response = Response.status(Response.Status.NOT_FOUND).build();
            } else {
                response = Response.status(Response.Status.OK).entity(exampleEntity).build();
            }
            asyncResponse.resume(response);
        }).start();
    }

    @Path(MyEntityResourcePathId)
    @DELETE
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Operation(summary = "Delete ExampleEntity by Id",
            tags = {"Id UUID"},
            description = "Delete the ExampleEntity and connected entities by ExampleEntity Id",
            responses = {
                    @ApiResponse(description = "", content = @Content(
                            schema = @Schema(implementation = ExampleEntity.class)
                    )),
                    @ApiResponse(responseCode = "400", description = "No Id supplied"),
                    @ApiResponse(responseCode = "404", description = "ExampleEntity not found")
            })
    public void DeleteEntityById(@Suspended final AsyncResponse asyncResponse,
                                 @Parameter(
                                         description = "Id of ExampleEntity",
                                         schema = @Schema(
                                                 type = "UUID",
                                                 format = "String",
                                                 description = "Id of ExampleEntity to be deleted"),
                                         required = true)
                                 @NotEmpty(message ="Id cannot be null") @PathParam("id") UUID id) {
        asyncResponse.setTimeoutHandler(asyncResponse1 -> asyncResponse1.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Operation time out.").build()));
        asyncResponse.setTimeout(20, TimeUnit.SECONDS);
        new Thread(() -> {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            ExampleEntity exampleEntity = entityManager.find(ExampleEntity.class, id);
            Response response;
            if (exampleEntity != null) {
                entityManager.remove(exampleEntity);
                response = Response.status(Response.Status.OK).build();
            } else {
                response = Response.status(Response.Status.NOT_FOUND).build();
            }
            entityManager.close();
            asyncResponse.resume(response);
        }).start();
    }

    @PreDestroy
    public void PreDestroyMyEntityResource()
    {
        entityManagerFactory.close();
    }
}
