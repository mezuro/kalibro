package org.kalibro.desktop.swingextension.table;

import java.awt.Component;

public class DefaultRenderer extends StandardRenderer {

	private static final StandardRenderer[] KNOWN_RENDERERS = new StandardRenderer[]{
		new BooleanRenderer(),
		new CollectionRenderer(),
		new DoubleRenderer(),
		new NullRenderer(),
		new StringRenderer()
	};

	@Override
	boolean canRender(Object value) {
		return true;
	}

	@Override
	public Component render(Object value) {
		for (StandardRenderer renderer : KNOWN_RENDERERS)
			if (renderer.canRender(value))
				return renderer.render(value);
		return new StringRenderer().render(value.toString());
	}
}