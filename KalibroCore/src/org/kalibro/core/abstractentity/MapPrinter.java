package org.kalibro.core.abstractentity;

import java.util.Map;

/**
 * Printer for maps.
 * 
 * @author Carlos Morais
 */
class MapPrinter extends Printer<Map<?, ?>> {

	@Override
	protected boolean canPrint(Object object) {
		return object instanceof Map;
	}

	@Override
	protected void doPrint(Map<?, ?> map, String comment) {
		if (map.isEmpty()) {
			printString(" {}");
			printComment(comment);
		} else
			printMap(map, comment);

	}

	private void printMap(Map<?, ?> map, String comment) {
		printComment(comment);
		for (Object key : map.keySet()) {
			newLine();
			printString(key + ":");
			printSubItem(map.get(key));
		}
	}
}