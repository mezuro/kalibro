package org.kalibro.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.service.entities.*;

@WebService
public class EchoEndpoint {

	@WebMethod
	@WebResult(name = "baseTool")
	public BaseToolXml echoBaseTool(@WebParam(name = "baseTool") BaseToolXml baseTool) {
		return baseTool;
	}

	@WebMethod
	@WebResult(name = "configuration")
	public ConfigurationXml echoConfiguration(@WebParam(name = "configuration") ConfigurationXml configuration) {
		return configuration;
	}

	@WebMethod
	@WebResult(name = "metricConfiguration")
	public MetricConfigurationXml echoMetricConfiguration(
		@WebParam(name = "metricConfiguration") MetricConfigurationXml metricConfiguration) {
		return metricConfiguration;
	}

	@WebMethod
	@WebResult(name = "moduleResult")
	public ModuleResultXml echoModuleResult(@WebParam(name = "moduleResult") ModuleResultXml moduleResult) {
		return moduleResult;
	}

	@WebMethod
	@WebResult(name = "project")
	public ProjectXml echoProject(@WebParam(name = "project") ProjectXml project) {
		return project;
	}

	@WebMethod
	@WebResult(name = "projectResult")
	public ProjectResultXml echoProjectResult(@WebParam(name = "projectResult") ProjectResultXml projectResult) {
		return projectResult;
	}

	@WebMethod
	@WebResult(name = "project")
	public RawProjectXml echoRawProject(@WebParam(name = "project") RawProjectXml project) {
		return project;
	}

	@WebMethod
	@WebResult(name = "repositoryType")
	public RepositoryType echoRepositoryType(RepositoryType repositoryType) {
		return repositoryType;
	}
}