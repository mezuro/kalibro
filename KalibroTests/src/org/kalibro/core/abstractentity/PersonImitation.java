package org.kalibro.core.abstractentity;

class PersonImitation extends AbstractEntity<PersonImitation> {

	@IdentityField
	protected String identityNumber;

	protected PersonImitation(Person person) {
		this.identityNumber = person.getIdentityNumber();
	}
}