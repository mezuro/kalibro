package org.kalibro.desktop.swingextension;

import static java.awt.RenderingHints.*;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.kalibro.desktop.KalibroDesktop;

public class Icon extends ImageIcon {

	public static final String KALIBRO = "kalibro.gif";

	public Icon(String resource) {
		super(KalibroDesktop.class.getResource(resource));
	}

	private Icon(Image image) {
		super(image);
	}

	public Icon scaleForSize(int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D graphics = createGraphics(image);
		graphics.drawImage(getImage(), createTransform(width, height), new JPanel());
		graphics.dispose();
		return new Icon(image);
	}

	private Graphics2D createGraphics(BufferedImage image) {
		Graphics2D graphics = image.createGraphics();
		graphics.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC);
		return graphics;
	}

	private AffineTransform createTransform(int width, int height) {
		double widthScale = ((double) width) / ((double) getIconWidth());
		double heightScale = ((double) height) / ((double) getIconHeight());
		return AffineTransform.getScaleInstance(widthScale, heightScale);
	}
}