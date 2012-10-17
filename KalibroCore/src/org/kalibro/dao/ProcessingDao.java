package org.kalibro.dao;

import java.util.Date;

import org.kalibro.ProcessState;
import org.kalibro.Processing;

/**
 * Data access object for {@link Processing}.
 * 
 * @author Carlos Morais
 */
public interface ProcessingDao {

	boolean hasProcessing(Long repositoryId);

	boolean hasReadyProcessing(Long repositoryId);

	boolean hasProcessingAfter(Date date, Long repositoryId);

	boolean hasProcessingBefore(Date date, Long repositoryId);

	ProcessState lastProcessingState(Long repositoryId);

	Processing lastReadyProcessing(Long repositoryId);

	Processing firstProcessing(Long repositoryId);

	Processing lastProcessing(Long repositoryId);

	Processing firstProcessingAfter(Date date, Long repositoryId);

	Processing lastProcessingBefore(Date date, Long repositoryId);
}