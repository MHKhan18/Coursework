package edu.stevens.cs594.chat.messages.api;

import edu.stevens.cs594.chat.messages.domain.Message;
import edu.stevens.cs594.chat.messages.domain.MessageFactory;
import edu.stevens.cs594.chat.messages.domain.dao.IMessageDao;
import edu.stevens.cs594.chat.service.dto.MessageDto;
import edu.stevens.cs594.chat.service.dto.MessageDtoFactory;
import edu.stevens.cs594.chat.service.dto.RoleDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.security.enterprise.SecurityContext;

@RequestScoped
@Transactional
@Path("/messages")
public class RemoteMessageService {
	
	private static final Logger logger = Logger.getLogger(RemoteMessageService.class.getCanonicalName());
	
	@Inject
	private IMessageDao messageDao;
	
	@Context
	private UriInfo uriInfo;

	@Inject
	private SecurityContext securityContext;


	@GET
	@Produces("application/json")
	// TODO restrict access to RoleDto.ROLE_POSTER or ROLE_MODERATOR
	@RolesAllowed({RoleDto.ROLE_MODERATOR, RoleDto.ROLE_POSTER})
	public Response getMessages() {
		// logger.info("Executing  getMessages");
		// if (!(securityContext.isCallerInRole(RoleDto.ROLE_MODERATOR) || securityContext.isCallerInRole(RoleDto.ROLE_POSTER))){
		// 	return Response.status(401, "User is neither moderator nor poster. user: " + securityContext.getCallerPrincipal().getName()).build();
		// }
		logger.info("Getting a list of all messages.");
		List<Message> messages = messageDao.getMessages();
		List<MessageDto> messageDtos = new ArrayList<MessageDto>();
		MessageDtoFactory messageDtoFactory = new MessageDtoFactory();
		for (Message message : messages) {
			MessageDto messageDto = messageDtoFactory.createMessageDto();
			messageDto.setId(message.getMessageId());
			messageDto.setSender(message.getSender());
			messageDto.setText(message.getText());
			messageDto.setTimestamp(message.getTimestamp());
			messageDtos.add(messageDto);
		}
		GenericEntity<List<MessageDto>> ms = new GenericEntity<List<MessageDto>>(messageDtos) {};
		return Response.ok(ms).build();
	}

	@POST
	@Consumes("application/json")
	// TODO restrict access to RoleDto.ROLE_POSTER
	@RolesAllowed({RoleDto.ROLE_POSTER})
	public Response addMessage(MessageDto dto) {
		// logger.info("Executing  addMessage");
		// if (!(securityContext.isCallerInRole(RoleDto.ROLE_POSTER))){
		// 	return Response.status(401, "user is not poster. User: " + securityContext.getCallerPrincipal().getName()).build();
		// }
		logger.info("Adding message: " + dto.getText());
		Message message = new MessageFactory().createMessage();
		message.setMessageId(UUID.randomUUID());
		message.setSender(dto.getSender());
		message.setText(dto.getText());
		message.setTimestamp(dto.getTimestamp());
		messageDao.addMessage(message);

		URI uri = uriInfo.getBaseUriBuilder().path("{id}").build(message.getMessageId());
		return Response.created(uri).build();
	}

	@DELETE
	@Path("{id}")
	// TODO restrict access to RoleDto.ROLE_MODERATOR
	@RolesAllowed({RoleDto.ROLE_MODERATOR})
	public Response deleteMessage(@PathParam("id") UUID id) {
		// logger.info("Executing  deleteMessage");
		// if (!(securityContext.isCallerInRole(RoleDto.ROLE_MODERATOR))){
		// 	return Response.status(401, "user is not moderator. User: " + securityContext.getCallerPrincipal().getName()).build();
		// }
		logger.info("Deleting message, id="+id);
		if (id == null) {
			logger.log(Level.SEVERE, "Missing message id for deleteMessage");
			return Response.status(Status.BAD_REQUEST).build();
		}
		messageDao.deleteMessage(id);
		return Response.ok().build();
	}

	@DELETE
	// TODO restrict access to RoleDto.ROLE_ADMIN
	@RolesAllowed({RoleDto.ROLE_ADMIN})
	public Response deleteMessages() {
		// logger.info("Executing  deleteMessages");
		// if (!(securityContext.isCallerInRole(RoleDto.ROLE_ADMIN))){
		// 	return Response.status(401, "user is not admin. User: " + securityContext.getCallerPrincipal().getName()).build();
		// }
		logger.info("Deleting messages as part of initialization");
		messageDao.deleteMessages();
		return Response.ok().build();
	}

}
