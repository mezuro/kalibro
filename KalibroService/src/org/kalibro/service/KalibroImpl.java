package org.kalibro.service;

import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Implementation of {@link Kalibro}.
 * 
 * @author Carlos Morais
 */
@WebService(name = "Kalibro", serviceName = "KalibroService")
public class KalibroImpl implements Kalibro {

	@Override
	@WebResult(name = "version")
	public String version() {
		return "1.0";
	}
}