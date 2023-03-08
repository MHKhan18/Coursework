package edu.stevens.cs594.chat.domain;

import java.time.Instant;
import java.util.Date;


public interface IMessageFactory {
	
	public Message createMessage ();
	
}
