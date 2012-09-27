package org.kalibro.dto;

import java.util.*;

import org.kalibro.KalibroError;

/**
 * Abstract class for data transfer objects (DTOs).
 * 
 * @author Carlos Morais
 */
public abstract class DataTransferObject<ENTITY> {

	public static <ENTITY> List<ENTITY> toList(Collection<? extends DataTransferObject<ENTITY>> dtos) {
		return convert(dtos, new ArrayList<ENTITY>());
	}

	public static <ENTITY> Set<ENTITY> toSet(Collection<? extends DataTransferObject<ENTITY>> dtos) {
		return convert(dtos, new HashSet<ENTITY>());
	}

	public static <ENTITY> SortedSet<ENTITY> toSortedSet(Collection<? extends DataTransferObject<ENTITY>> dtos) {
		return convert(dtos, new TreeSet<ENTITY>());
	}

	public static <ENTITY, DTO extends DataTransferObject<ENTITY>> List<DTO> createDtos(
		Collection<ENTITY> entities, Class<DTO> dtoClass) {
		List<DTO> dtos = new ArrayList<DTO>();
		for (ENTITY entity : entities)
			dtos.add(createDto(entity, dtoClass));
		return dtos;
	}

	private static <ENTITY, COLLECTION extends Collection<ENTITY>> COLLECTION convert(
		Collection<? extends DataTransferObject<ENTITY>> dtos, COLLECTION entities) {
		for (DataTransferObject<ENTITY> dto : dtos)
			entities.add(dto.convert());
		return entities;
	}

	private static <ENTITY, DTO extends DataTransferObject<ENTITY>> DTO createDto(ENTITY entity, Class<DTO> dtoClass) {
		try {
			return dtoClass.getDeclaredConstructor(entity.getClass()).newInstance(entity);
		} catch (Exception exception) {
			throw new KalibroError("Could not create DTO.", exception);
		}
	}

	public abstract ENTITY convert();
}