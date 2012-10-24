package org.kalibro.core.abstractentity;

import java.util.Map;

/**
 * Determines equality of maps. Two maps are equals if they have the same key set, and each key maps to the same
 * elements.
 * 
 * @author Carlos Morais
 */
class MapEquality extends Equality<Map<?, ?>> {

	private boolean deep;

	public MapEquality(boolean deep) {
		this.deep = deep;
	}

	@Override
	protected boolean canEvaluate(Object value) {
		return value instanceof Map;
	}

	@Override
	protected boolean equals(Map<?, ?> map, Map<?, ?> other) {
		if (!areEqual(map.keySet(), other.keySet(), deep))
			return false;
		for (Object key : map.keySet())
			if (!areEqual(map.get(key), other.get(key), deep))
				return false;
		return true;
	}
}