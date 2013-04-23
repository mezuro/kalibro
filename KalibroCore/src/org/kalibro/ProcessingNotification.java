package org.kalibro;

import java.io.File;

import javax.mail.Message.RecipientType;

import org.codemonkey.simplejavamail.Email;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.SortingFields;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProcessingNotificationDao;
import org.kalibro.dao.RepositoryDao;

@SortingFields({"repository_id", "name"})
public class ProcessingNotification extends AbstractEntity<ProcessingNotification> {

	public static ProcessingNotification importFrom(File file) {
		return importFrom(file, ProcessingNotification.class);
	}

	private static ProcessingNotificationDao dao() {
		return DaoFactory.getProcessingNotificationDao();
	}

	private static RepositoryDao repositoryDao() {
		return DaoFactory.getRepositoryDao();
	}

	private Long id;

	private Long repositoryId;
	private String name;
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRepositoryId() {
		return repositoryId;
	}

	public void setRepositoryId(Long repositoryId) {
		this.repositoryId = repositoryId;
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
		Repository repository = repositoryDao().get(repositoryId);
		Email emailToSend = new Email();
		emailToSend.setSubject("Repository " + repository.getCompleteName() + " processing results");
		emailToSend.setText("Processing results in repository " + repository.getName() + " has finished succesfully.");
		emailToSend.addRecipient(getName(), getEmail(), RecipientType.TO);
		return emailToSend;
	}

	public void save() {
		throwExceptionIf(repositoryId == null, "Notification is not related to any repository.");
		id = dao().save(this, repositoryId);
	}

	public void delete() {
		dao().delete(id);
	}
}
