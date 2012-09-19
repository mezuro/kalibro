package org.kalibro.service;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Root end point of the service. Just responds the version of Kalibro.
 * 
 * @author Carlos Morais
 */
@WebService(name = "Kalibro", serviceName = "KalibroService")
public interface Kalibro {

	@WebMethod
	@WebResult(name = "version")
	String version();
}