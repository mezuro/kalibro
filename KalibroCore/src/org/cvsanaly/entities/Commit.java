package org.cvsanaly.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "scmlog")
public class Commit {
	@Id
	private long id;
	
	@Column(name = "rev")
	private String revision;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}
}
