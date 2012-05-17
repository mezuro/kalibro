package org.cvsanaly;

import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.kalibro.core.util.Identifier;

public enum CVSAnalyMetric {
	// TODO Add more metrics
	NUMBER_OF_SOURCE_LINES_OF_CODE,
	NUMBER_OF_LINES_OF_CODE,
	NUMBER_OF_COMMENTS,
	NUMBER_OF_COMMENTED_LINES,
	NUMBER_OF_BLANK_LINES,
	NUMBER_OF_FUNCTIONS;

	@Override
	public String toString() {
		return Identifier.fromConstant(name()).asText();
	}

	protected NativeMetric getNativeMetric() {
		NativeMetric nativeMetric = new NativeMetric(toString(), Granularity.CLASS, Language.values());
		nativeMetric.setOrigin("CVSAnaly");
		return nativeMetric;
	}
}