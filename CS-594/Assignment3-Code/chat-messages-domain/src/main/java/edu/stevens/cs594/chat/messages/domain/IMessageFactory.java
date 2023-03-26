package edu.stevens.cs594.chat.messages.domain;

import java.time.OffsetDateTime;


public interface IMessageFactory {
	
	public Message createMessage ();
	
}
