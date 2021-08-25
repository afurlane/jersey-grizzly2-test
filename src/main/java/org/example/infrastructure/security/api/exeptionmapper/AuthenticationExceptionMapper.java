package org.example.infrastructure.security.api.exeptionmapper;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.infrastructure.model.ApiErrorDetails;
import org.example.infrastructure.security.exception.AuthenticationException;

/**
 * Exception mapper for {@link AuthenticationException}s.
 *
 * @author cassiomolin
 */
@Provider
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(AuthenticationException exception) {

        Response.Status status = Response.Status.FORBIDDEN;

        ApiErrorDetails errorDetails = new ApiErrorDetails();
        errorDetails.setStatus(status.getStatusCode());
        errorDetails.setTitle(status.getReasonPhrase());
        errorDetails.setMessage(exception.getMessage());
        errorDetails.setPath(uriInfo.getAbsolutePath().getPath());

        return Response.status(status).entity(errorDetails).type(MediaType.APPLICATION_JSON).build();
    }
}