package edu.stevens.cs594.chat.messages.domain.dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Qualifier;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Session Bean implementation class PatientProducer
 */
@RequestScoped
@Transactional
public class ChatMessageDomainProducer {

    /**
     * Default constructor. 
     */
    public ChatMessageDomainProducer() {
    }
    
    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)  
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})  
    public @interface ChatMessageDomain {}
    
    @PersistenceContext(unitName="chat-domain-messages")
    EntityManager em;
    
    @Produces
    @ChatMessageDomain
    public EntityManager chatDomainProducer() {
    	return em;
    }
    
    public void chatDomainDispose(@Disposes @ChatMessageDomain EntityManager em) {
    	// em.close();
    }

}
