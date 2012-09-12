package org.kalibro.service;

import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public class KalibroMetricsImpl implements KalibroMetrics {

	@Override
	@WebResult(name = "version")
	public String version() {
		return "0.6";
	}
}