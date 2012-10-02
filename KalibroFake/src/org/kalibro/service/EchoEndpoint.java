package org.kalibro.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.*;
import org.kalibro.service.xml.*;

@WebService
public class EchoEndpoint {

	@WebMethod
	@WebResult(name = "baseTool")
	public BaseToolXml echoBaseTool(@WebParam(name = "baseTool") BaseToolXml baseTool) {
		return baseTool;
	}

	@WebMethod
	@WebResult(name = "configuration")
	public ConfigurationXmlRequest echoConfiguration(
		@WebParam(name = "configuration") ConfigurationXmlRequest configuration) {
		Configuration entity = configuration.convert();
		entity.setName("echo " + entity.getName());
		return new ConfigurationXmlRequest(entity);
	}

	@WebMethod
	@WebResult(name = "metricConfiguration")
	public MetricConfigurationXmlRequest echoMetricConfiguration(
		@WebParam(name = "metricConfiguration") MetricConfigurationXmlRequest metricConfiguration) {
		MetricConfiguration entity = metricConfiguration.convert();
		entity.setCode("echo_" + entity.getCode());
		return new MetricConfigurationXmlRequest(entity);
	}

	@WebMethod
	@WebResult(name = "moduleResult")
	public ModuleResultXml echoModuleResult(@WebParam(name = "moduleResult") ModuleResultXml moduleResult) {
		ModuleResult entity = moduleResult.convert();
		Module module = entity.getModule();
		module.setName("echo." + module.getLongName());
		return new ModuleResultXml(entity);
	}

	@WebMethod
	@WebResult(name = "project")
	public ProjectXmlResponse echoProject(@WebParam(name = "project") ProjectXmlResponse project) {
		Project entity = project.convert();
		entity.setName("echo " + entity.getName());
		return new ProjectXmlResponse(entity);
	}

	@WebMethod
	@WebResult(name = "projectResult")
	public ProjectResultXml echoProjectResult(@WebParam(name = "projectResult") ProjectResultXml projectResult) {
		ProjectResult entity = projectResult.convert();
		Project project = entity.getProject();
		project.setName("echo " + project.getName());
		return new ProjectResultXml(entity);
	}

	@WebMethod
	@WebResult(name = "project")
	public ProjectXmlRequest echoRawProject(@WebParam(name = "project") ProjectXmlRequest project) {
		Project entity = project.convert();
		entity.setName("echo " + entity.getName());
		return new ProjectXmlRequest(entity);
	}

	@WebMethod
	@WebResult(name = "parentGranularity")
	public Granularity inferParentGranularity(@WebParam(name = "granularity") Granularity granularity) {
		return granularity.inferParentGranularity();
	}
}