package org.kalibro.spago;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.Project;
import org.kalibro.service.KalibroEndpoint;
import org.kalibro.service.ModuleResultEndpoint;
import org.kalibro.service.ProjectEndpoint;
import org.kalibro.service.ProjectResultEndpoint;
import org.kalibro.service.entities.RawProjectXml;

class KalibroClientForSpago {

	private String serviceAddress;

	private KalibroEndpoint kalibroPort;
	private ProjectEndpoint projectPort;
	private ModuleResultEndpoint moduleResultPort;
	private ProjectResultEndpoint projectResultPort;

	protected KalibroClientForSpago(String serviceAddress) throws MalformedURLException {
		this.serviceAddress = serviceAddress;
		kalibroPort = getEndpointPort(KalibroEndpoint.class);
		projectPort = getEndpointPort(ProjectEndpoint.class);
		moduleResultPort = getEndpointPort(ModuleResultEndpoint.class);
		projectResultPort = getEndpointPort(ProjectResultEndpoint.class);
	}

	private <T> T getEndpointPort(Class<T> endpointClass) throws MalformedURLException {
		String namespace = "http://service.kalibro.org/";
		String endpointName = endpointClass.getSimpleName();
		URL wsdlLocation = new URL(serviceAddress + endpointName + "/?wsdl");
		QName serviceName = new QName(namespace, endpointName + "ImplService");
		QName portName = new QName(namespace, endpointName + "ImplPort");
		return Service.create(wsdlLocation, serviceName).getPort(portName, endpointClass);
	}

	protected boolean hasResultsFor(String projectName) {
		return projectResultPort.hasResultsFor(projectName);
	}

	protected void saveAndProcess(Project project) {
		projectPort.saveProject(new RawProjectXml(project));
		kalibroPort.processProject(project.getName());
	}

	protected ModuleResult getLastApplicationResult(String projectName) {
		Date lastResultDate = projectResultPort.getLastResultOf(projectName).convert().getDate();
		return moduleResultPort.getModuleResult(projectName, projectName, lastResultDate).convert();
	}
}