package org.kalibro;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingFields;

/**
 * Results of processing a {@link Repository}.
 * 
 * @author Carlos Morais
 */
@SortingFields({"project", "date"})
public class RepositoryResult extends AbstractEntity<RepositoryResult> {

	@IdentityField
	private Project project;

	@IdentityField
	private Date date;

	private RepositoryState state;
	private Throwable error;

	private Map<RepositoryState, Long> stateTimes;
	private ModuleNode sourceTree;

	public RepositoryResult(Project project) {
		setProject(project);
		setDate(new Date());
		setState(RepositoryState.NEW);
		stateTimes = new HashMap<RepositoryState, Long>();
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public RepositoryState getState() {
		if (error != null)
			return RepositoryState.ERROR;
		return state;
	}

	public String getStateMessage() {
		return getState().getMessage(repository.getCompleteName());
	}

	public RepositoryState getStateWhenErrorOcurred() {
		assertHasError();
		return state;
	}

	public void setState(RepositoryState state) {
		if (state == RepositoryState.ERROR)
			throw new KalibroException("Use setError(Throwable) to put repository in error state");
		error = null;
		this.state = state;
	}

	public Throwable getError() {
		assertHasError();
		return error;
	}

	private void assertHasError() {
		if (error == null)
			throw new KalibroException("Repository " + repository.getCompleteName() + " has no error.");
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public Long getLoadTime() {
		assertProcessed();
		return stateTimes.get(RepositoryState.LOADING);
	}

	public Long getCollectTime() {
		assertProcessed();
		return stateTimes.get(RepositoryState.COLLECTING);
	}

	public Long getAnalysisTime() {
		assertProcessed();
		return stateTimes.get(RepositoryState.ANALYZING);
	}

	public void setStateTime(RepositoryState state, long time) {
		stateTimes.put(state, time);
	}

	public ModuleNode getSourceTree() {
		assertProcessed();
		return sourceTree;
	}

	public void setSourceTree(ModuleNode sourceTree) {
		this.sourceTree = sourceTree;
	}

	private void assertProcessed() {
		if (!isProcessed())
			throw new KalibroException("Project not yet processed: " + project.getName());
	}

	public boolean isProcessed() {
		return sourceTree != null;
	}
}