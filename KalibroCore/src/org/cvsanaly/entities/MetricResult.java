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
	private org.cvsanaly.entities.File file;
	@Column(name = "commit_id")
	private org.cvsanaly.entities.Commit commit;
	
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
}
