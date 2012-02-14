package org.kalibro.client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.kalibro.Kalibro;

public class EndpointPortFactory {

	private static final String NAMESPACE = "http://service.kalibro.org/";

	public static <T> T getEndpointPort(Class<T> endpointClass) {
		String endpointName = endpointClass.getSimpleName();
		URL wsdlLocation = getWsdlLocation(endpointName);
		QName serviceName = new QName(NAMESPACE, endpointName + "ImplService");
		QName portName = new QName(NAMESPACE, endpointName + "ImplPort");
		return Service.create(wsdlLocation, serviceName).getPort(portName, endpointClass);
	}

	private static <T> URL getWsdlLocation(String endpointName) {
		try {
			String serviceAddress = Kalibro.currentSettings().getServiceAddress();
			return new URL(serviceAddress + endpointName + "/?wsdl");
		} catch (MalformedURLException exception) {
			throw new IllegalArgumentException(exception);
		}
	}
}