package org.cvsanaly.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "metrics")
public class MetricResult {

	@Id
	private long id;

	@Column(name = "file_id")
	private RepositoryFile file;

	@Column(name = "commit_id")
	private Commit commit;

	@Column(name = "sloc")
	private int sloc;

	@Column(name = "loc")
	private int loc;

	@Column(name = "ncomment")
	private int ncomment;

	@Column(name = "lcomment")
	private int lcomment;

	@Column(name = "lblank")
	private int lblank;

	@Column(name = "nfunctions")
	private int nfunctions;

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

	public int getSloc() {
		return sloc;
	}

	public void setSloc(int sloc) {
		this.sloc = sloc;
	}

	public int getLoc() {
		return loc;
	}

	public void setLoc(int loc) {
		this.loc = loc;
	}

	public int getNcomment() {
		return ncomment;
	}

	public void setNcomment(int ncomment) {
		this.ncomment = ncomment;
	}

	public int getLcomment() {
		return lcomment;
	}

	public void setLcomment(int lcomment) {
		this.lcomment = lcomment;
	}

	public int getLblank() {
		return lblank;
	}

	public void setLblank(int lblank) {
		this.lblank = lblank;
	}

	public int getNfunctions() {
		return nfunctions;
	}

	public void setNfunctions(int nfunctions) {
		this.nfunctions = nfunctions;
	}
}
