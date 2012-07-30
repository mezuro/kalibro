package org.cvsanaly.entities;

import javax.persistence.*;

@Entity
@Table(name = "metrics")
@NamedQuery(
	  name = "getAllMetricResults",
	  query = "Select metric from MetricResult metric"
	)
public class MetricResult {

	@Id
	private long id;

	@PrimaryKeyJoinColumn(name = "file_id")
	private RepositoryFile file;

	@PrimaryKeyJoinColumn(name = "commit_id")
	private Commit commit;

	@Column(name = "sloc")
	private double numberOfSourceCodeLines;
	@Column(name = "loc")
	private double numberOfLinesOfCode;
	@Column(name = "ncomment")
	private double numberOfComments;
	@Column(name = "lcomment")
	private double numberOfCommentedLines;
	@Column(name = "lblank")
	private double numberOfBlankLines;
	@Column(name = "nfunctions")
	private double numberOfFunctions;
	
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
	
	public double getNumberOfSourceCodeLines() {
		return numberOfSourceCodeLines;
	}
	
	public void setNumberOfSourceCodeLines(double numberOfSourceCodeLines) {
		this.numberOfSourceCodeLines = numberOfSourceCodeLines;
	}
	
	public double getNumberOfLinesOfCode() {
		return numberOfLinesOfCode;
	}
	
	public void setNumberOfLinesOfCode(double numberOfLinesOfCode) {
		this.numberOfLinesOfCode = numberOfLinesOfCode;
	}
	
	public double getNumberOfComments() {
		return numberOfComments;
	}
	
	public void setNumberOfComments(double numberOfComments) {
		this.numberOfComments = numberOfComments;
	}
	
	public double getNumberOfCommentedLines() {
		return numberOfCommentedLines;
	}
	
	public void setNumberOfCommentedLines(double numberOfCommentedLines) {
		this.numberOfCommentedLines = numberOfCommentedLines;
	}
	
	public double getNumberOfBlankLines() {
		return numberOfBlankLines;
	}
	
	public void setNumberOfBlankLines(double numberOfBlankLines) {
		this.numberOfBlankLines = numberOfBlankLines;
	}
	
	public double getNumberOfFunctions() {
		return numberOfFunctions;
	}
	
	public void setNumberOfFunctions(double numberOfFunctions) {
		this.numberOfFunctions = numberOfFunctions;
	}
	
}
