package edu.stevens.cs594.chat.messages.restclient;

public class PersistenceMessageExn extends Exception {

	private static final long serialVersionUID = 3927784502194720492L;

	public PersistenceMessageExn (String msg, Exception e) {
		super(msg, e);
	}
	
}
