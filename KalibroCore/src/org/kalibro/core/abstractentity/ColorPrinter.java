package org.kalibro.core.abstractentity;

import java.awt.Color;

/**
 * Printer for {@link Color}.
 * 
 * @author Carlos Morais
 */
class ColorPrinter extends Printer<Color> {

	@Override
	protected boolean canPrint(Object object) {
		return object instanceof Color;
	}

	@Override
	protected void doPrint(Color color, String comment) {
		printString(" 0x" + Integer.toHexString(color.getRGB()).substring(2));
		printComment(comment);
	}
}