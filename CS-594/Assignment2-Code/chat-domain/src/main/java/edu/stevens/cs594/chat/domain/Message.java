package edu.stevens.cs594.chat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;


/**
 * Entity implementation class for Entity: Message
 *
 */
@Entity

@NamedQueries({
		@NamedQuery(
				name = "SearchMessages",
				query = "select m from Message m"),
		@NamedQuery(
				name = "CountMessagesByMessageId",
				query = "select count(m) from Message m where m.messageId = :messageId"),
		@NamedQuery(
				name="SearchMessagesByMessageId",
				query="select m from Message m where m.messageId = :messageId"),
		@NamedQuery(
				name = "DeleteMessages",
				query = "delete from Message m"),
})

@Table(indexes = @Index(columnList = "messageId"))
public class Message implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final int MESSAGE_LENGTH = 60;
	
	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	private UUID messageId;
	
	@Column(length = MESSAGE_LENGTH)
	private String text;
	
	@Column(length = User.USER_NAME_LENGTH)
	private String sender;

	/*
	 * JPA does not support Instant.
	 */
	private OffsetDateTime timestamp;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UUID getMessageId() {
		return messageId;
	}

	public void setMessageId(UUID messageId) {
		this.messageId = messageId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public OffsetDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(OffsetDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Message() {
		super();
	}
   
}
