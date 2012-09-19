package org.kalibro.dto;

import org.kalibro.ReadingGroup;

class ReadingGroupDtoStub extends ReadingGroupDto {

	private ReadingGroup group;

	ReadingGroupDtoStub(ReadingGroup group) {
		this.group = group;
	}

	@Override
	public Long id() {
		return group.getId();
	}

	@Override
	public String name() {
		return group.getName();
	}

	@Override
	public String description() {
		return group.getDescription();
	}
}