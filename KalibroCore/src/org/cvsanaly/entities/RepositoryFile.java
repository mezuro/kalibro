package org.cvsanaly.entities;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class RepositoryFile {

	@Id
	@Column(name = "id")
	private long id;

	@Column(name = "file_name")
	private String filename;

	@OneToMany(mappedBy = "file")
	private List<FileLink> fileLinks;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public List<FileLink> getFileLinks() {
		return fileLinks;
	}

	public void setFileLinks(List<FileLink> fileLinks) {
		this.fileLinks = fileLinks;
	}
}