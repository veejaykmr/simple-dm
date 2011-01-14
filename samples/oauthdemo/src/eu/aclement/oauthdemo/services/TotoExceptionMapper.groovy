package eu.aclement.oauthdemo.services

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

class TotoExceptionMapper implements ExceptionMapper<TotoExceptionMapper> {
	public Response toResponse(TotoExceptionMapper exception) {
        return Response.serverError().build()
    }
}
