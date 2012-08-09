package br.jabuti;

import static org.kalibro.core.model.enums.Granularity.APPLICATION;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;

public class JabutiOutputParser {

	private Map<String, NativeMetric> supportedMetrics;

	public Collection<NativeMetric> getSupportedMetrics(InputStream metricListOutput) throws IOException {
		supportedMetrics = new LinkedHashMap<String, NativeMetric>(); 
		List<String> lines = IOUtils.readLines(metricListOutput);
		for (String line : lines)
			if (line.contains(" - "))
				parseSupportedMetric(line);
		
		return supportedMetrics.values();		
	}
	
	private void parseSupportedMetric(String line) {
		int hyphenIndex = line.indexOf(" - ");
		String code = line.substring(0, hyphenIndex).trim();
		String name = line.substring(hyphenIndex + 3).trim();
		Granularity scope = APPLICATION;
		NativeMetric metric = new NativeMetric(name, scope, Language.JAVA);
		metric.setOrigin("Jabuti");
		supportedMetrics.put(code, metric);
	}

	public Set<NativeModuleResult> parseResults(InputStream jabutiOuput,
			Set<NativeMetric> metrics) {
		// TODO Auto-generated method stub
		return null;
	}

}
