package org.kalibro.core.abstractentity;

import java.util.Map;

/**
 * Determines equality of maps deeply for each mapping.
 * 
 * @author Carlos Morais
 */
class MapEquality extends Equality<Map<?, ?>> {

	@Override
	protected boolean canEvaluate(Object value) {
		return value instanceof Map;
	}

	@Override
	protected boolean equals(Map<?, ?> map, Map<?, ?> other) {
		if (!areDeepEqual(map.keySet(), other.keySet()))
			return false;
		for (Object key : map.keySet())
			if (!areDeepEqual(map.get(key), other.get(key)))
				return false;
		return true;
	}
}