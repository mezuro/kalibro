package org.kalibro.desktop;

import java.awt.Image;

import org.kalibro.desktop.swingextension.Icon;

public final class KalibroIcons {

	public static final String KALIBRO = "kalibro.gif";

	public static Image image(String name) {
		return icon(name).getImage();
	}

	public static Icon icon(String name) {
		return new Icon(KalibroIcons.class.getResource(name));
	}

	private KalibroIcons() {
		return;
	}
}