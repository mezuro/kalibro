package org.kalibro.core.dto;

import org.kalibro.ReadingGroup;

class ReadingGroupDtoStub extends ReadingGroupDto {

	private ReadingGroup group;

	ReadingGroupDtoStub(ReadingGroup group) {
		this.group = group;
	}

	@Override
	protected Long id() {
		return group.getId();
	}

	@Override
	protected String name() {
		return group.getName();
	}

	@Override
	protected String description() {
		return group.getDescription();
	}
}