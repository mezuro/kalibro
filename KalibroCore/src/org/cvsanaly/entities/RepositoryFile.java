package org.cvsanaly.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "files")
public class RepositoryFile {
	@Id
	private long id;
	
	@Column(name = "file_name")
	private String filename;

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
}
