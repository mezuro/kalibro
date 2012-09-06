package org.kalibro.core.dto;

import java.util.Arrays;
import java.util.List;

class PerfectNumbersFetcher extends ListFetcher<Integer> {

	private int fetchCount;

	@Override
	protected List<Integer> fetch() {
		fetchCount++;
		return Arrays.asList(6, 28, 496);
	}

	protected int fetchCount() {
		return fetchCount;
	}
}