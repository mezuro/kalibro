package org.kalibro;

import java.io.File;

import javax.mail.Message.RecipientType;

import org.codemonkey.simplejavamail.Email;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.SortingFields;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProcessingNotificationDao;

@SortingFields("name")
public class ProcessingNotification extends AbstractEntity<ProcessingNotification> {

	public static ProcessingNotification importFrom(File file) {
		return importFrom(file, ProcessingNotification.class);
	}

	private static ProcessingNotificationDao dao() {
		return DaoFactory.getProcessingNotificationDao();
	}

	private Long id;

	private String name;
	private String email;
	
	
	public ProcessingNotification() {
		super();
	}
	
	public ProcessingNotification(String name, String email) {
		setName(name);
		setEmail(email);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Email createEmail(Repository repository) {
		Email emailToSend = new Email();
		emailToSend.setSubject("Repository " + repository.getCompleteName() + " processing results");
		emailToSend.setText("Processing results in repository " + repository.getName() + " has finished succesfully.");
		emailToSend.addRecipient(getName(), getEmail(), RecipientType.TO);
		return emailToSend;
	}

	public void save(Repository repository) {
		throwExceptionIf(repository == null, "Notification is not related to any repository.");
		id = dao().save(this, repository.getId());
	}

	public void delete() {
		dao().delete(id);
	}
}
