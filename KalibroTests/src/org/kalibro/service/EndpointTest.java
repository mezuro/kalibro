package org.kalibro.service;

import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.kalibro.TestCase;

public abstract class EndpointTest extends TestCase {

	private static final String NAMESPACE = "http://service.kalibro.org/";

	private static PrintStream out, err;

	@BeforeClass
	public static void suppressOutput() {
		out = System.out;
		err = System.err;
		System.setOut(null);
		System.setErr(null);
	}

	@AfterClass
	public static void restoreOutput() {
		System.setOut(out);
		System.setErr(err);
	}

	private Endpoint endpoint;

	@After
	public void tearDown() {
		endpoint.stop();
	}

	protected <T> T publishAndGetPort(T implementor, Class<T> endpointClass) throws MalformedURLException {
		String endpointName = endpointClass.getSimpleName();
		URL wsdlLocation = publish(implementor, endpointName);
		QName serviceName = new QName(NAMESPACE, endpointName + "ImplService");
		QName portName = new QName(NAMESPACE, endpointName + "ImplPort");
		return Service.create(wsdlLocation, serviceName).getPort(portName, endpointClass);
	}

	private URL publish(Object implementor, String endpointName) throws MalformedURLException {
		String address = "http://localhost:8080/KalibroService/" + endpointName + "/";
		endpoint = Endpoint.create(implementor);
		endpoint.publish(address);
		return new URL(address + "?wsdl");
	}
}