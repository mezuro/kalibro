package org.kalibro.core.model;

import static org.kalibro.core.model.MetricConfigurationFixtures.thresholds;

import java.util.*;

public final class RangeFixtures {

	public static Range newRange(String code, RangeLabel type) {
		return type.createRange(thresholds(code));
	}

	public static Set<Range> newRanges(Double... thresholds) {
		Set<Range> ranges = new TreeSet<Range>();
		for (RangeLabel rangeLabel : getRangeTypes(thresholds.length))
			ranges.add(rangeLabel.createRange(thresholds));
		return ranges;
	}

	private static List<RangeLabel> getRangeTypes(int numberOfThresholds) {
		List<RangeLabel> rangeLabels = new ArrayList<RangeLabel>(Arrays.asList(RangeLabel.values()));
		if (numberOfThresholds < 4)
			rangeLabels.remove(RangeLabel.GOOD);
		if (numberOfThresholds < 3)
			rangeLabels.remove(RangeLabel.WARNING);
		return rangeLabels;
	}

	private RangeFixtures() {
		return;
	}
}