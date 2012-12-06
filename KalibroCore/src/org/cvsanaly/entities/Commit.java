package org.cvsanaly.entities;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "scmlog")
public class Commit {

	@Id
	@Column(name = "id")
	private long id;

	@Column(name = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date revision) {
		this.date = revision;
	}
}