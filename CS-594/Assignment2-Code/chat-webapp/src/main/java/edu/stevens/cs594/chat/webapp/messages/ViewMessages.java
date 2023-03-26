package edu.stevens.cs594.chat.webapp.messages;

import edu.stevens.cs594.chat.service.IMessageService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import java.security.Principal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import edu.stevens.cs594.chat.service.dto.MessageDto;
import edu.stevens.cs594.chat.service.dto.MessageDtoFactory;
import edu.stevens.cs594.chat.webapp.BaseBacking;

@Named("messagesBacking")
@ViewScoped
public class ViewMessages extends BaseBacking {

	private static final long serialVersionUID = -1983439889541606510L;
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ViewMessages.class.getCanonicalName());

	@Inject
	private SecurityContext securityContext;
	
	private String username;
	
	/*
	 * For new messages, when the user is a poster.
	 */
	private String text;
	
	/*
	 * List of messages.  Each line has a boolean for deletion by a moderator.
	 */
	private List<MessageDto> messages;
	
	public String getUsername() {
		return username;
	}

	public List<MessageDto> getMessages() {
		return this.messages;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Inject
	private IMessageService messageService;
	
	private MessageDtoFactory messageDtoFactory = new MessageDtoFactory();
	
	/**
	 * Refresh the messages from the database.
	 */
	public void refreshMessages() {
		messages = messageService.getMessages();
	}
	
	/**
	 * Invoked by poster to post a new message.
	 */
	public void postMessage() {
		MessageDto message = messageDtoFactory.createMessageDto();
		message.setSender(username);
		message.setTimestamp(OffsetDateTime.now());
		message.setText(text);
		messageService.addMessage(message);
		text = "";
		refreshMessages();
	}
	
	/**
	 * Invoked by moderator to delete a message.
	 */
	public void deleteMessage(UUID id) {
		messageService.deleteMessage(id);		
		refreshMessages();
	}
	
	@PostConstruct
	private void init() {
		// TODO set this.username to be the currently logged-in user, and refresh messages.
		username = securityContext.getCallerPrincipal().getName();
		refreshMessages();
	}

}
