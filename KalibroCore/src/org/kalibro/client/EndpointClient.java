package org.kalibro.client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.kalibro.KalibroException;

/**
 * Abstract client for Kalibro Service end points.
 * 
 * @author Carlos Morais
 */
abstract class EndpointClient<ENDPOINT> {

	private static final String NAMESPACE = "http://service.kalibro.org/";

	protected ENDPOINT port;

	protected EndpointClient(String serviceAddress, Class<ENDPOINT> endpointClass) {
		String endpointName = endpointClass.getSimpleName();
		URL wsdlLocation = getWsdlLocation(endpointName, serviceAddress);
		QName serviceName = new QName(NAMESPACE, endpointName + "Service");
		QName portName = new QName(NAMESPACE, endpointName + "Port");
		port = Service.create(wsdlLocation, serviceName).getPort(portName, endpointClass);
	}

	private URL getWsdlLocation(String endpointName, String serviceAddress) {
		try {
			return new URL(serviceAddress + endpointName + "/?wsdl");
		} catch (MalformedURLException exception) {
			throw new KalibroException("Invalid service address: " + serviceAddress, exception);
		}
	}
}