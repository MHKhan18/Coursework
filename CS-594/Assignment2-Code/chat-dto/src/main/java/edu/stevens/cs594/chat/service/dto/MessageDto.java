package edu.stevens.cs594.chat.service.dto;

import jakarta.json.bind.annotation.JsonbDateFormat;
import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;


public class MessageDto implements Serializable {

    private final static long serialVersionUID = 1L;

    protected UUID id;

    protected String text;

    protected String sender;

    // @JsonbDateFormat(JsonbDateFormat.TIME_IN_MILLIS)
    protected OffsetDateTime timestamp;

    public UUID getId() {
        return id;
    }

    public void setId(UUID value) {
        this.id = value;
    }

    public boolean isSetId() {
        return true;
    }

    public String getText() {
        return text;
    }

    public void setText(String value) {
        this.text = value;
    }

    public boolean isSetText() {
        return (this.text!= null);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String value) {
        this.sender = value;
    }

    public boolean isSetSender() {
        return (this.sender!= null);
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime value) {
        this.timestamp = value;
    }

    public boolean isSetTimestamp() {
        return (this.timestamp!= null);
    }

    /*
     * To display timestamp, need to specify a time zone.  We use system default.
     */
    public ZonedDateTime getZonedTimestamp() {
    	return timestamp.atZoneSameInstant(ZoneId.systemDefault());
    }

}
