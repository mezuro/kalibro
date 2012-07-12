package org.kalibro;

/**
 * Intended to be in .gitignore and be configured for different developer machines.
 * 
 * @author Carlos Morais
 */
public interface Timeouts {

	long UNIT_TIMEOUT = 750;
	long INTEGRATION_TIMEOUT = 2500;
	long ACCEPTANCE_TIMEOUT = 10000;
}