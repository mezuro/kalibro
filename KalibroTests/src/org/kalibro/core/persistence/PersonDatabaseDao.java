package org.kalibro.core.persistence;

import javax.persistence.Entity;

import org.kalibro.core.dto.DataTransferObject;
import org.kalibro.core.persistence.PersonDatabaseDao.Person;
import org.kalibro.core.persistence.PersonDatabaseDao.PersonRecord;

class PersonDatabaseDao extends DatabaseDao<Person, PersonRecord> {

	public PersonDatabaseDao(RecordManager recordManager) {
		super(recordManager, PersonRecord.class);
	}

	@Entity(name = "Person")
	class PersonRecord implements DataTransferObject<Person> {

		@Override
		public Person convert() {
			return null;
		}
	}

	@SuppressWarnings("all")
	class Person {}
}