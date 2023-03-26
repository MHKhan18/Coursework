package edu.stevens.cs594.chat.messages.domain.dao;

public class MessageExn extends Exception {
	private static final long serialVersionUID = 1L;
	public MessageExn (String msg) {
		super(msg);
	}
}