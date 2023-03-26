package edu.stevens.cs594.chat.messages.domain.dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;

import edu.stevens.cs594.chat.messages.domain.Message;
import edu.stevens.cs594.chat.messages.domain.dao.ChatMessageDomainProducer.ChatMessageDomain;
import java.util.UUID;

@RequestScoped
public class MessageDao implements IMessageDao {
	
	@Inject
	@ChatMessageDomain
	private EntityManager em;

	@Override
	public List<Message> getMessages() {
		TypedQuery<Message> query = em.createNamedQuery("SearchMessages", Message.class);
		return query.getResultList();
	}

	@Override
	public void addMessage(Message m) {
		UUID id = m.getMessageId();
		Query query = em.createNamedQuery("CountMessagesByMessageId").setParameter("messageId", id);
		Long numExisting = (Long) query.getSingleResult();

		if (numExisting < 1) {
			em.persist(m);
		} else {
			throw new IllegalArgumentException("Insertion: Message with  id (" + id + ") already exists.");
		}
	}
	
	@Override
	public void deleteMessage(UUID id) {
		TypedQuery<Message> query = em.createNamedQuery("SearchMessagesByMessageId", Message.class).setParameter("messageId",id);
		List<Message> messages = query.getResultList();

		if (messages.size() > 1) {
			throw new IllegalArgumentException("Duplicate message records: id = " + id);
		} else if (messages.size() < 1) {
			throw new IllegalArgumentException("Message not found: id = " + id);
		} else {
			Message m = messages.get(0);
			em.remove(m);
		}
	}

	@Override
	public void deleteMessages() {
		Query q = em.createNamedQuery("DeleteMessages");
		q.executeUpdate();
	}

}
