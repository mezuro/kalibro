package org.kalibro;

import java.awt.Color;

public enum RangeLabel {

	EXCELLENT {

		@Override
		protected Range createRange(Double... thresholds) {
			return new Range(0.0, thresholds[0], "Excellent", 10.0, Color.GREEN);
		}
	},
	GOOD {

		@Override
		protected Range createRange(Double... thresholds) {
			return new Range(thresholds[0], thresholds[1], "Good", 8.0, new Color(127, 255, 0));
		}
	},
	REGULAR {

		@Override
		protected Range createRange(Double... thresholds) {
			int middle = thresholds.length / 2;
			return new Range(thresholds[middle - 1], thresholds[middle], "Regular", 5.0, Color.YELLOW);
		}
	},
	WARNING {

		@Override
		protected Range createRange(Double... thresholds) {
			int last = thresholds.length - 1;
			return new Range(thresholds[last - 1], thresholds[last], "Warning", 3.0, new Color(255, 127, 0));
		}
	},
	BAD {

		@Override
		protected Range createRange(Double... thresholds) {
			int last = thresholds.length - 1;
			return new Range(thresholds[last], Double.POSITIVE_INFINITY, "Bad", 0.0, Color.RED);
		}
	};

	protected abstract Range createRange(Double... thresholds);
}