package org.kalibro.core.abstractentity;

class NoIdentityEntity extends AbstractEntity<NoIdentityEntity> {

	protected String field1;

	@Print(skip = true)
	protected String field2;
}