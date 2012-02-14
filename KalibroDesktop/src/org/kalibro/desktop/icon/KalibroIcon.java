package org.kalibro.desktop.icon;

import javax.swing.ImageIcon;

public class KalibroIcon extends AbstractIcon {

	public KalibroIcon() {
		super("kalibro.gif");
	}

	public ImageIcon scaleForInternalFrame() {
		return super.scale(0.3);
	}
}