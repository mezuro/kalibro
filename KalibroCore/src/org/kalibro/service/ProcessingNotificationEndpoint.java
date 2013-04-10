package org.kalibro.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.kalibro.service.xml.ProcessingNotificationXml;

@WebService(name = "ProcessingNotificationEndpoint", serviceName = "ProcessingNotificationEndpointService")
public interface ProcessingNotificationEndpoint {
	
	@WebMethod
	@WebResult(name = "processingNotification")
	List<ProcessingNotificationXml> allProcessingNotifications();
}
