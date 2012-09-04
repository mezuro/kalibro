package org.kalibro.core.persistence.dao;

import org.kalibro.Reading;

public interface ReadingDao {

	void save(Reading reading);

	void delete(Reading reading);
}