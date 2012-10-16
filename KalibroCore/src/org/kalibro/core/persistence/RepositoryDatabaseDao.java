package org.kalibro.core.persistence;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.TypedQuery;

import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.core.Identifier;
import org.kalibro.core.loaders.RepositoryLoader;
import org.kalibro.core.persistence.record.RepositoryRecord;
import org.kalibro.dao.RepositoryDao;
import org.kalibro.dto.DataTransferObject;

/**
 * Database access implementation for {@link RepositoryDao}.
 * 
 * @author Carlos Morais
 */
class RepositoryDatabaseDao extends DatabaseDao<Repository, RepositoryRecord> implements RepositoryDao {

	RepositoryDatabaseDao(RecordManager recordManager) {
		super(recordManager, RepositoryRecord.class);
	}

	@Override
	public SortedSet<RepositoryType> supportedTypes() {
		SortedSet<RepositoryType> supportedTypes = new TreeSet<RepositoryType>();
		for (RepositoryType type : RepositoryType.values())
			if (isSupported(type))
				supportedTypes.add(type);
		return supportedTypes;
	}

	private boolean isSupported(RepositoryType type) {
		try {
			String typeName = Identifier.fromConstant(type.name()).asClassName();
			String loaderName = "org.kalibro.core.loaders." + typeName + "Loader";
			RepositoryLoader loader = (RepositoryLoader) Class.forName(loaderName).newInstance();
			loader.validate();
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	@Override
	public Repository repositoryOf(Long processingId) {
		TypedQuery<RepositoryRecord> query;
		query = createRecordQuery("JOIN Processing processing WHERE processing.id = :processingId");
		query.setParameter("processingId", processingId);
		return query.getSingleResult().convert();
	}

	@Override
	public SortedSet<Repository> repositoriesOf(Long projectId) {
		TypedQuery<RepositoryRecord> query = createRecordQuery("WHERE repository.project.id = :projectId");
		query.setParameter("projectId", projectId);
		return DataTransferObject.toSortedSet(query.getResultList());
	}

	@Override
	public Long save(Repository repository, Long projectId) {
		return save(new RepositoryRecord(repository, projectId)).id();
	}

	@Override
	public void process(Long repositoryId) {
		// TODO Auto-generated method stub
	}

	@Override
	public void cancelProcessing(Long repositoryId) {
		// TODO Auto-generated method stub
	}
}