package org.kalibro.core.dto;

/**
 * Interface for DTOs (Data Transfer Objects).
 * 
 * @author Carlos Morais
 */
public interface DataTransferObject<ENTITY> {

	ENTITY convert();
}