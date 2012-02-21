package org.kalibro.core.model;

import java.util.*;

public class RangeFixtures {

	public static Range amlocRange(RangeLabel type) {
		return type.createRange(7.0, 10.0, 13.0, 19.5);
	}

	public static Set<Range> createRanges(Double... thresholds) {
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
}