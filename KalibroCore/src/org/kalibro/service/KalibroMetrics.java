package org.kalibro.service;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Root end point of the service. Just responds the version of Kalibro Metrics.
 * 
 * @author Carlos Morais
 */
@WebService(name = "KalibroMetrics")
public interface KalibroMetrics {

	@WebMethod
	@WebResult(name = "version")
	String version();
}