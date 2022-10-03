package br.com.jersey.demo.demojersey.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ExceptionMapperGeneric implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        return Response.serverError().entity(e.getMessage()).build();
    }
}
