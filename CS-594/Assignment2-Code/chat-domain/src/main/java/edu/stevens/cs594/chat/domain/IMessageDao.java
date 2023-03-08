package edu.stevens.cs594.chat.domain;

import java.util.List;
import java.util.UUID;

public interface IMessageDao {
	
	public static class MessageExn extends Exception {
		private static final long serialVersionUID = 1L;
		public MessageExn (String msg) {
			super(msg);
		}
	}
	
	public List<Message> getMessages();

	public void addMessage (Message m);
	
	public void deleteMessage (UUID id);
	
	public void deleteMessages ();
	
}
