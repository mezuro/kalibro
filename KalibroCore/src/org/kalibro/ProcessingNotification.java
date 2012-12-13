package org.kalibro;

import javax.mail.Message.RecipientType;

import org.codemonkey.simplejavamail.Email;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.SortingFields;

@SortingFields({"repository","name"})
public class ProcessingNotification extends AbstractEntity<Repository> {
	private Long id;
	
	private Repository repository;
	private String name;
	private String email;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Repository getRepository() {
		return repository;
	}
	
	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Email createEmail() {
		Email emailToSend = new Email();
		emailToSend.setSubject("Repository " + repository.getCompleteName() + " processing results");
		emailToSend.setText("Processing results in repository " + repository.getName() + " has finished succesfully.");
		emailToSend.addRecipient(getName(), getEmail(), RecipientType.TO);
		return emailToSend;
	}
	
}
