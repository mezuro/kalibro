package org.kalibro.core.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Abstract class for data transfer objects (DTOs).
 * 
 * @author Carlos Morais
 */
public abstract class DataTransferObject<ENTITY> {

	public static <ENTITY> List<ENTITY> convert(Collection<? extends DataTransferObject<ENTITY>> dtos) {
		List<ENTITY> entities = new ArrayList<ENTITY>();
		for (DataTransferObject<ENTITY> dto : dtos)
			entities.add(dto.convert());
		return entities;
	}

	public abstract ENTITY convert();
}