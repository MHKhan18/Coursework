package edu.stevens.cs594.chat.domain.dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Qualifier;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Session Bean implementation class PatientProducer
 */
@RequestScoped
public class ChatUserDomainProducer {

    /**
     * Default constructor. 
     */
    public ChatUserDomainProducer() {
    }
    
    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)  
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})  
    public @interface ChatUserDomain {}
    
    @PersistenceContext(unitName="chat-domain-users")
    EntityManager em;
    
    @Produces
    @ChatUserDomain
    public EntityManager chatDomainProducer() {
    	return em;
    }
    
    public void chatDomainDispose(@Disposes @ChatUserDomain EntityManager em) {
    	// em.close();
    }

}
