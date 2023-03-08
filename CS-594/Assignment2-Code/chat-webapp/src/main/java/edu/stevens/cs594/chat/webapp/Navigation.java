package edu.stevens.cs594.chat.webapp;

public class Navigation {

	/*
	 * These action strigs define part of an implicit navigation logic.
	 */
	
	/*
	 * Login controller: Which page to go to when someone logs in.
	 */
	public static final String ADMIN = "admin";
	public static final String MODERATOR = "moderator";
	public static final String POSTER = "poster";

	/*
	 * Adding a user: Start at the page for adding details, then page with QR code.
	 */
	public static final String ADD_USER = "add-user";
	public static final String ADD_USER_QRCODE = "add-user-qrcode";

}
