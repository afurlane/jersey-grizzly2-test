package org.example;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.List;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

    @GET
    @Path("tryQuery")
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Operation(summary = "Get an array of strings from query param",
            tags = {"Array"},
            description = "Does nothing whith that",
            responses = {
                    @ApiResponse(description = "The array", content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = String.class))
                    )),
                    @ApiResponse(responseCode = "400", description = "No array supplied since is required"),
                    @ApiResponse(responseCode = "404", description = "Some other error because we don't find something")
            })
    public List<String> getItWithQuery(
            @Parameter(
            description = "An array of strings",
            array = @ArraySchema(
                schema = @Schema(
                        type = "String",
                        format = "String",
                        description = "Array to be searched")
            ),
            required = true)
            @QueryParam("array") List<String> parameters)
    {
        return parameters;
    }

}
