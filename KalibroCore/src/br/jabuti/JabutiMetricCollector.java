package br.jabuti;

import java.io.File;
import java.util.Set;

import org.kalibro.core.MetricCollector;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;

public class JabutiMetricCollector implements MetricCollector {

	private BaseTool baseTool;
	private final JabutiOutputParser outputParser = new JabutiOutputParser();
	
	@Override
	public BaseTool getBaseTool() {
		if (null == baseTool) {
			this.baseTool = new BaseTool("Jabuti");
			this.baseTool.setCollectorClass(JabutiMetricCollector.class);
			this.baseTool.setSupportedMetrics(outputParser.getSupportedMetrics());
		}

		return baseTool;
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory,
			Set<NativeMetric> metrics) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
