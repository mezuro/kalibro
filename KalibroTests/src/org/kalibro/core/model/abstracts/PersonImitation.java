package org.kalibro.core.model.abstracts;

public class PersonImitation extends AbstractEntity<PersonImitation> {

	@IdentityField
	protected String identityNumber;

	public PersonImitation(Person person) {
		this.identityNumber = person.getIdentityNumber();
	}
}