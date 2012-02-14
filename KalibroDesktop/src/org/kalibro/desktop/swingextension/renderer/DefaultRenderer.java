package org.kalibro.desktop.swingextension.renderer;

import java.awt.Component;

public class DefaultRenderer extends StandardRenderer {

	private StandardRenderer[] knownRenderers = new StandardRenderer[]{
		new BooleanRenderer(),
		new DoubleRenderer(),
		new NullRenderer(),
		new StringRenderer()
	};

	@Override
	protected boolean canRender(Object value) {
		return true;
	}

	@Override
	protected Component render(Object value) {
		for (StandardRenderer renderer : knownRenderers)
			if (renderer.canRender(value))
				return renderer.render(value);
		return new StringRenderer().render(value.toString());
	}
}