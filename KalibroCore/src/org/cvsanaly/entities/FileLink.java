package org.cvsanaly.entities;

import javax.persistence.*;

@Entity
@Table(name = "file_links")
public class FileLink {

	@Id
	@Column(name = "id")
	private long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "file_id", referencedColumnName = "id")
	private RepositoryFile file;

	@ManyToOne(optional = false)
	@JoinColumn(name = "commit_id", referencedColumnName = "id")
	private Commit commit;

	@Column(name = "file_path")
	private String filePath;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public RepositoryFile getFile() {
		return file;
	}

	public void setFile(RepositoryFile file) {
		this.file = file;
	}

	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}