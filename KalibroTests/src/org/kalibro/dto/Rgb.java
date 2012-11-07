package org.kalibro.dto;

import java.awt.Color;

class Rgb extends DataTransferObject<Color> {

	int rgb;

	Rgb(Color color) {
		rgb = color.getRGB();
	}

	@Override
	public Color convert() {
		return new Color(rgb);
	}
}