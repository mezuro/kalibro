package org.kalibro;

import java.util.SortedSet;

import javax.mail.Message.RecipientType;

import org.codemonkey.simplejavamail.Email;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.Print;
import org.kalibro.core.abstractentity.SortingFields;
import org.kalibro.core.concurrent.TaskListener;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.processing.MailSender;
import org.kalibro.core.processing.ProcessTask;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.RepositoryListenerDao;

/**
 * Listener of {@link Repository} processings.
 * 
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
@SortingFields("name")
public class RepositoryListener extends AbstractEntity<RepositoryListener> implements TaskListener<Void> {

	public static SortedSet<RepositoryListener> all() {
		return dao().all();
	}

	private static RepositoryListenerDao dao() {
		return DaoFactory.getRepositoryListenerDao();
	}

	@Print(skip = true)
	private Long id;

	@Print(order = 1)
	private String name;

	@Print(order = 2)
	private String email;

	private static final String NOREPLY = "\n\nThis is an automatic message. Please, do not reply.";

	public RepositoryListener() {
		this("New name", "New email");
	}

	public RepositoryListener(String name, String email) {
		setName(name);
		setEmail(email);
	}

	public Long getId() {
		return id;
	}

	public boolean hasId() {
		return id != null;
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

	private void sendEmail(Repository repository, ProcessState processState) {
		Email emailToSend = MailSender.createEmptyEmailWithSender();

		String status;
		if (processState.equals(ProcessState.READY))
			status = " has finished successfully.";
		else
			status = " has resulted in error.";

		emailToSend.setSubject(repository.getCompleteName() + " processing results");
		emailToSend.setText("Processing results in repository " + repository.getName() +
			status + NOREPLY);
		emailToSend.addRecipient(getName(), getEmail(), RecipientType.TO);

		MailSender.sendEmail(emailToSend);
	}

	public void save(Repository repository) {
		throwExceptionIf(repository == null, "Listener is not related to any repository.");
		throwExceptionIf(name.trim().isEmpty(), "RepositoryListener requires name.");
		throwExceptionIf(email.trim().isEmpty(), "RepositoryListener requires email.");
		repository.assertSaved();
		id = dao().save(this, repository.getId());
	}

	public void delete() {
		if (hasId())
			dao().delete(id);
		deleted();
	}

	private void deleted() {
		id = null;
	}

	@Override
	public void taskFinished(TaskReport<Void> report) {
		ProcessTask task = (ProcessTask) report.getTask();
		sendEmail(task.getRepository(), task.getProcessState());
	}
}