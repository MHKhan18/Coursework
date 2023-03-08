package edu.stevens.cs594.chat.domain;

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
public class ChatDomainProducer {

    /**
     * Default constructor. 
     */
    public ChatDomainProducer() {
    }
    
    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)  
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})  
    public @interface ChatDomain {}
    
    @PersistenceContext(unitName="chat-domain")
    EntityManager em;
    
    @Produces
    @ChatDomain
    public EntityManager chatDomainProducer() {
    	return em;
    }
    
    public void chatDomainDispose(@Disposes @ChatDomain EntityManager em) {
    	// em.close();
    }

}
