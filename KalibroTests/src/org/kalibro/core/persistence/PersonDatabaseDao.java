package org.kalibro.core.persistence;

import javax.persistence.Entity;

import org.kalibro.core.persistence.PersonDatabaseDao.Person;
import org.kalibro.core.persistence.PersonDatabaseDao.PersonRecord;
import org.kalibro.dto.DataTransferObject;

class PersonDatabaseDao extends DatabaseDao<Person, PersonRecord> {

	PersonDatabaseDao() {
		super(PersonRecord.class);
	}

	@Entity(name = "Person")
	class PersonRecord extends DataTransferObject<Person> {

		@Override
		public Person convert() {
			return null;
		}
	}

	@SuppressWarnings("all")
	class Person {}
}