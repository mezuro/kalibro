package org.kalibro.core.persistence.record;

import javax.persistence.*;

import org.kalibro.ProcessingNotification;
import org.kalibro.dto.ProcessingNotificationDto;

@Entity(name = "ProcessingNotification")
@Table(name = "\"processing_notification\"")
public class ProcessingNotificationRecord extends ProcessingNotificationDto {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "processing_notification")
	@TableGenerator(name = "processing_notification", table = "sequences", pkColumnName = "table_name",
		valueColumnName = "sequence_count", pkColumnValue = "processing_notification", initialValue = 1,
		allocationSize = 1)
	@Column(name = "\"id\"", nullable = false, unique = true)
	private Long id;

	@Column(name = "\"repository_id\"", nullable = false)
	private Long repositoryId;

	@Column(name = "\"name\"", nullable = false)
	private String name;

	@Column(name = "\"email\"", nullable = false)
	private String email;

	public ProcessingNotificationRecord() {
		super();
	}

	public ProcessingNotificationRecord(ProcessingNotification processingNotification, Long repositoryId) {
		id = processingNotification.getId();
		this.repositoryId = repositoryId;
		name = processingNotification.getName();
		email = processingNotification.getEmail();
	}

	@Override
	public Long id() {
		return id;
	}

	public Long repositoryId() {
		return repositoryId;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String email() {
		return email;
	}

}
