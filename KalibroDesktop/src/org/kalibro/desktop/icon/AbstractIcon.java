package org.kalibro.desktop.icon;

import static java.awt.RenderingHints.*;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class AbstractIcon extends ImageIcon {

	public AbstractIcon(String source) {
		super(AbstractIcon.class.getResource(source));
	}

	public ImageIcon scale(double scale) {
		BufferedImage scaledImage = createScaledImage(scale);
		Graphics2D graphics = createGraphicsFor(scaledImage);
		graphics.drawImage(getImage(), getTransform(scale), getObserver());
		graphics.dispose();
		return new ImageIcon(scaledImage);
	}

	private BufferedImage createScaledImage(double scale) {
		int width = (int) (scale * getImage().getWidth(getObserver()));
		int height = (int) (scale * getImage().getHeight(getObserver()));
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
	}

	private ImageObserver getObserver() {
		return new JLabel();
	}

	private Graphics2D createGraphicsFor(BufferedImage scaledImage) {
		Graphics2D graphics = scaledImage.createGraphics();
		graphics.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC);
		return graphics;
	}

	private AffineTransform getTransform(double scale) {
		return AffineTransform.getScaleInstance(scale, scale);
	}
}