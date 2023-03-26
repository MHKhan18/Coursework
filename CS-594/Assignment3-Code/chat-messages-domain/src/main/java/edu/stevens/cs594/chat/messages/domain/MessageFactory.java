package edu.stevens.cs594.chat.messages.domain;

import java.time.OffsetDateTime;

public class MessageFactory implements IMessageFactory {

	@Override
	public Message createMessage() {
		return new Message();
	}


}
