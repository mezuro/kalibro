package org.kalibro.desktop;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JColorChooser;

import org.fest.swing.core.ComponentMatcher;
import org.fest.swing.core.TypeMatcher;
import org.fest.swing.fixture.DialogFixture;

public class FestUtils {

	public static void setColor(DialogFixture fixture, String buttonName, Color color) {
		fixture.button(buttonName).click();
		JColorChooser chooser = (JColorChooser) fixture.robot.finder().find(new ComponentMatcher() {

			@Override
			public boolean matches(Component component) {
				return "colorChooser".equals(component.getName());
			}
		});
		chooser.setColor(color);
		fixture.button("colorChooserOk").click();
	}
}