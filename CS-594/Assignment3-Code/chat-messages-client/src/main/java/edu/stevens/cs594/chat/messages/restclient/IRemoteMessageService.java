package edu.stevens.cs594.chat.messages.restclient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import edu.stevens.cs594.chat.service.dto.MessageDto;

@RegisterRestClient
@Path("/messages")
public interface IRemoteMessageService {
	
	@GET
	@Produces("application/json")
	public List<MessageDto> getMessages(@HeaderParam("Authorization") String authHeader);
	
	@POST
	@Consumes("application/json")
	public void addMessage (@HeaderParam("Authorization") String authHeader, MessageDto m);
	
	@DELETE
	@Path("{id}")
	public void deleteMessage (@HeaderParam("Authorization") String authHeader, @PathParam("id") UUID id);
	
	@DELETE
	public Response deleteMessages (@HeaderParam("Authorization") String authHeader);

}
