package org.kalibro;

import java.awt.Color;

public enum RangeLabel {

	EXCELLENT {

		@Override
		protected Range createRange(Double... thresholds) {
			return newRange(0.0, thresholds[0], "Excellent", 10.0, Color.GREEN);
		}
	},
	GOOD {

		@Override
		protected Range createRange(Double... thresholds) {
			return newRange(thresholds[0], thresholds[1], "Good", 8.0, new Color(127, 255, 0));
		}
	},
	REGULAR {

		@Override
		protected Range createRange(Double... thresholds) {
			int middle = thresholds.length / 2;
			return newRange(thresholds[middle - 1], thresholds[middle], "Regular", 5.0, Color.YELLOW);
		}
	},
	WARNING {

		@Override
		protected Range createRange(Double... thresholds) {
			int last = thresholds.length - 1;
			return newRange(thresholds[last - 1], thresholds[last], "Warning", 3.0, new Color(255, 127, 0));
		}
	},
	BAD {

		@Override
		protected Range createRange(Double... thresholds) {
			int last = thresholds.length - 1;
			return newRange(thresholds[last], Double.POSITIVE_INFINITY, "Bad", 0.0, Color.RED);
		}
	};

	protected abstract Range createRange(Double... thresholds);

	private static Range newRange(Double beginning, Double end, String label, Double grade, Color color) {
		Range range = new Range(beginning, end);
		range.setReading(new Reading(label, grade, color));
		return range;
	}
}