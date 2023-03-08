package edu.stevens.cs594.chat.domain;

import java.time.Instant;
import java.util.Date;

public class MessageFactory implements IMessageFactory {

	@Override
	public Message createMessage() {
		return new Message();
	}

}
