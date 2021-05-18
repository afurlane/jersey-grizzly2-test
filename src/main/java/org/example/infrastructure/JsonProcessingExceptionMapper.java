package org.example.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {

        public static class Error {
            public String key;
            public String message;
        }

        @Override
        public Response toResponse(JsonProcessingException exception) {
            Error error = new Error();
            error.key = "bad-json";
            error.message = exception.getMessage();
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
}
