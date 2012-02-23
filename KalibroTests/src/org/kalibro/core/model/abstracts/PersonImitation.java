package org.kalibro.core.model.abstracts;

class PersonImitation extends AbstractEntity<PersonImitation> {

	@IdentityField
	protected String identityNumber;

	protected PersonImitation(Person person) {
		this.identityNumber = person.getIdentityNumber();
	}
}