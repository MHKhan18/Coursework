package edu.stevens.cs594.chat.messages.domain.dao;

import java.util.List;

import edu.stevens.cs594.chat.messages.domain.Message;
import java.util.UUID;

public interface IMessageDao {
	
	public List<Message> getMessages();

	public void addMessage (Message t);
	
	public void deleteMessage (UUID id);
	
	public void deleteMessages ();
	
}
