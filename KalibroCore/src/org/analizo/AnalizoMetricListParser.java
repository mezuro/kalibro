package org.analizo;

import static org.kalibro.Granularity.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.kalibro.Granularity;
import org.kalibro.Language;
import org.kalibro.NativeMetric;

/**
 * Parser for Analizo metric list output.
 * 
 * @author Carlos Morais.
 */
class AnalizoMetricListParser {

	private Map<NativeMetric, String> supportedMetrics;

	AnalizoMetricListParser(InputStream input) throws IOException {
		supportedMetrics = new HashMap<NativeMetric, String>();
		List<String> lines = IOUtils.readLines(input);
		for (String line : lines)
			if (line.contains(" - "))
				parseSupportedMetric(line);
	}

	private void parseSupportedMetric(String line) {
		int hyphenIndex = line.indexOf('-');
		String code = line.substring(0, hyphenIndex).trim();
		String name = line.substring(hyphenIndex + 1).trim();
		Granularity scope = code.startsWith("total") ? SOFTWARE : CLASS;
		NativeMetric metric = new NativeMetric(name, scope, Language.C, Language.CPP, Language.JAVA);
		supportedMetrics.put(metric, code);
	}

	Map<NativeMetric, String> getSupportedMetrics() {
		return supportedMetrics;
	}
}