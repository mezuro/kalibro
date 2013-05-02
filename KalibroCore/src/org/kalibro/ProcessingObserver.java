package org.kalibro;

import java.io.File;

import javax.mail.Message.RecipientType;

import org.codemonkey.simplejavamail.Email;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.SortingFields;
import org.kalibro.core.processing.MailSender;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProcessingObserverDao;

@SortingFields("name")
public class ProcessingObserver extends AbstractEntity<ProcessingObserver>
			implements Observer<Repository, ProcessState> {
	
	public static ProcessingObserver importFrom(File file) {
		return importFrom(file, ProcessingObserver.class);
	}

	private static ProcessingObserverDao dao() {
		return DaoFactory.getProcessingObserverDao();
	}

	private Long id;

	private String name;
	private String email;
	
	private static final String NOREPLY = "\n\nThis is an automatic message." + 
		" Please, do not reply.";
	
	public ProcessingObserver() {
		super();
	}
	
	public ProcessingObserver(String name, String email) {
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

	public void sendEmail(Repository repository, ProcessState processState) {
		MailSender mailSender = new MailSender();
		Email emailToSend = mailSender.createEmptyEmailWithSender();
		
		String status;
		if (processState.equals(ProcessState.READY))
			status = " has finished succesfully.";
		else
			status = " has resulted in error.";
		
		emailToSend.setSubject(repository.getCompleteName() + " processing results");
		emailToSend.setText("Processing results in repository " + repository.getName() +
			status + NOREPLY);
		emailToSend.addRecipient(getName(), getEmail(), RecipientType.TO);
		
		mailSender.sendEmail(emailToSend);
	}

	public void save(Repository repository) {
		throwExceptionIf(repository == null, "Notification is not related to any repository.");
		id = dao().save(this, repository.getId());
	}

	public void delete() {
		dao().delete(id);
	}
	
	@Override
	public void update(Repository repository, ProcessState processState) {
		sendEmail(repository, processState);
	}
}