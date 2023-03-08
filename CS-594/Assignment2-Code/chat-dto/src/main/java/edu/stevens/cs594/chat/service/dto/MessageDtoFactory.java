package edu.stevens.cs594.chat.service.dto;

import edu.stevens.cs594.chat.domain.Message;

public class MessageDtoFactory {

	public MessageDto createMessageDto() {
		return new MessageDto();
	}
	
	public MessageDto createMessageDto(Message m) {
		MessageDto d = createMessageDto();
		d.setId(m.getMessageId());
		d.setSender(m.getSender());
		d.setText(m.getText());
		d.setTimestamp(m.getTimestamp());
		return d;
	}

}
