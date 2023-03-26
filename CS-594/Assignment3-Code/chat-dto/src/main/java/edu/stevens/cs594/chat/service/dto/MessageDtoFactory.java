package edu.stevens.cs594.chat.service.dto;

public class MessageDtoFactory {
	
	ObjectFactory factory;
	
	public MessageDtoFactory() {
		factory = new ObjectFactory();
	}
	
	public MessageDto createMessageDto() {
		return factory.createMessageDto();
	}

}
