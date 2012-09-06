package org.kalibro.core.dto;

import org.kalibro.ReadingGroup;

class DummyReadingGroupDto extends ReadingGroupDto {

	private ReadingGroup group;

	DummyReadingGroupDto(ReadingGroup group) {
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