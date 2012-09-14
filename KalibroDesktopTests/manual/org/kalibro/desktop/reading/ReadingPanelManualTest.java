package org.kalibro.desktop.reading;

import org.kalibro.Reading;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

public final class ReadingPanelManualTest extends ReadingPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ReadingPanel", new ReadingPanelManualTest()).setVisible(true);
	}

	private static Reading loadFixture() {
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD);
		return yaml.loadAs(Reading.class.getResourceAsStream("Reading-excellent.yml"), Reading.class);
	}

	private ReadingPanelManualTest() {
		super(loadFixture());
	}
}