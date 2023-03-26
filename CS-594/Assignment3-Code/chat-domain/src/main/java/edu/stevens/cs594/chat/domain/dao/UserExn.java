package edu.stevens.cs594.chat.domain.dao;

public class UserExn extends Exception {
	private static final long serialVersionUID = 1L;
	public UserExn (String msg) {
		super(msg);
	}
}