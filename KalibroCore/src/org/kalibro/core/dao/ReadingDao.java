package org.kalibro.core.dao;

import org.kalibro.Reading;

public interface ReadingDao {

	void save(Reading reading);

	void delete(Reading reading);
}