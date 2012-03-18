package org.kalibro.service;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import org.junit.BeforeClass;
import org.kalibro.KalibroTestCase;

public abstract class KalibroServiceTestCase extends KalibroTestCase {

	private static final String NAMESPACE = "http://service.kalibro.org/";

	@BeforeClass
	public static void suppressStandardOutput() {
		System.setOut(null);
	}

	protected <T> T publishAndGetPort(Object implementor, Class<T> endpointClass) throws MalformedURLException {
		String endpointName = endpointClass.getSimpleName();
		URL wsdlLocation = publish(implementor, endpointName);
		QName serviceName = new QName(NAMESPACE, endpointName + "ImplService");
		QName portName = new QName(NAMESPACE, endpointName + "ImplPort");
		return Service.create(wsdlLocation, serviceName).getPort(portName, endpointClass);
	}

	private URL publish(Object implementor, String endpointName) throws MalformedURLException {
		String address = "http://localhost:8080/KalibroService/" + endpointName + "/";
		Endpoint.publish(address, implementor);
		return new URL(address + "?wsdl");
	}
}