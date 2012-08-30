package org.cvsanaly.entities;

import java.lang.reflect.Field;

import javax.persistence.*;

@Entity
@Table(name = "metrics")
@NamedQuery(
	name = "getLastMetricResults",
	query = "SELECT NEW org.cvsanaly.entities.MetricResult(metric, links) FROM MetricResult metric, " +
			"IN(metric.file.fileLinks) links WHERE " +
			"metric.commit.date = " +
			"(SELECT max(metric2.commit.date) FROM MetricResult metric2 WHERE metric.file.id = metric2.file.id) AND " +
			"(links.commit.date = " +
			"(SELECT max(links2.commit.date) FROM FileLink links2 WHERE links2.file.id = metric.file.id))")
public class MetricResult {

	@Id
	@Column(name = "id")
	private long id;

	@PrimaryKeyJoinColumn(name = "file_id")
	private RepositoryFile file;

	@PrimaryKeyJoinColumn(name = "commit_id")
	private Commit commit;

	@Column(name = "sloc")
	private double numberOfSourceLinesOfCode;
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
	@Column(name = "mccabe_max")
	private double maximumCyclomaticComplexity;
	@Column(name = "mccabe_mean")
	private double averageCyclomaticComplexity;
	@Column(name = "halstead_vol")
	private double halsteadVolume;
	
	@Transient
	private String filePath;
	
	public MetricResult() {	}
	
	public MetricResult(MetricResult metricResult, FileLink fileLink) throws IllegalAccessException {
		for (Field field : getClass().getDeclaredFields()) {
			if (field.getAnnotations().length == 0)
				continue;
			field.set(this, field.get(metricResult));
		}
		this.filePath = fileLink.getFilePath();
	}

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

	public double getNumberOfSourceLinesOfCode() {
		return numberOfSourceLinesOfCode;
	}

	public void setNumberOfSourceLinesOfCode(double numberOfSourceLinesOfCode) {
		this.numberOfSourceLinesOfCode = numberOfSourceLinesOfCode;
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

	public double getMaximumCyclomaticComplexity() {
		return maximumCyclomaticComplexity;
	}

	public void setMaximumCyclomaticComplexity(double maximumCyclomaticComplexity) {
		this.maximumCyclomaticComplexity = maximumCyclomaticComplexity;
	}

	public double getAverageCyclomaticComplexity() {
		return averageCyclomaticComplexity;
	}

	public void setAverageCyclomaticComplexity(double averageCyclomaticComplexity) {
		this.averageCyclomaticComplexity = averageCyclomaticComplexity;
	}

	public double getHalsteadVolume() {
		return halsteadVolume;
	}

	public void setHalsteadVolume(double halsteadVolume) {
		this.halsteadVolume = halsteadVolume;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
