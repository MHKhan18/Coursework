package edu.stevens.cs594.chat.webapp;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;


import edu.stevens.cs594.chat.service.messages.Messages;

public class BaseBacking implements Serializable {
	
	private static final long serialVersionUID = 1819802919748985743L;
	
	public static final String CHARSET = "utf-8";
	
	@Inject
	private FacesContext facesContext;
	
	protected FacesContext getFacesContext() {
		// return FacesContext.getCurrentInstance();
		return facesContext;
	}

	protected ExternalContext getExternalContext() {
		return getFacesContext().getExternalContext();
	}

	protected HttpServletRequest getWebRequest() {
		return (HttpServletRequest) getExternalContext().getRequest();
	}
	
	protected HttpServletResponse getWebResponse() {
		return (HttpServletResponse) getExternalContext().getResponse();
	}
	
	/*
	 * Locale-specific feedback, optionally associated with a UI component.
	 */
	private static ResourceBundle getBundle(FacesContext context) {
		Locale locale = context.getViewRoot().getLocale();
		return ResourceBundle.getBundle(Messages.messagesPath, locale);
	}

	protected static void addMessageToContext(FacesContext context, String clientId, String key) {
		ResourceBundle res = getBundle(context);
		context.addMessage(clientId,  new FacesMessage(res.getString(key)));
	}
	
	protected void addMessage(String clientId, String key) {
		addMessageToContext(getFacesContext(), clientId, key);
	}
	
	protected String getClientId(String id) {
		return getFacesContext().getViewRoot().findComponent(id).getClientId(getFacesContext());
	}
	
	protected void addMessage(String key) {
		addMessage((String)null, key);
	} 
	
	public String getDisplayString(String key) {
		return key == null ? null : getBundle(getFacesContext()).getString(key);
	}
	
	protected static void reportValidationError(FacesContext context, UIComponent component, String key) {
		((UIInput) component).setValid(false);
		addMessageToContext(context, component.getClientId(context), key);
	}
		
}
