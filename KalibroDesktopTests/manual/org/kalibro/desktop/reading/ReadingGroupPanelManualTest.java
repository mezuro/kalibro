package org.kalibro.desktop.reading;

import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.list.TablePanelListener;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

public final class ReadingGroupPanelManualTest implements TablePanelListener<Reading> {

	public static void main(String[] args) {
		new ReadingGroupPanelManualTest();
	}

	private static ReadingGroup loadFixture() {
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD);
		return yaml.loadAs(ReadingGroup.class.getResourceAsStream("ReadingGroup-scholar.yml"), ReadingGroup.class);
	}

	private ReadingGroupPanelManualTest() {
		ReadingGroupPanel panel = new ReadingGroupPanel(loadFixture());
		panel.addReadingsListener(this);
		new ComponentWrapperDialog("ReadingGroupPanel", panel).setVisible(true);
	}

	@Override
	public void add() {
		System.out.println("Add reading");
	}

	@Override
	public void edit(Reading reading) {
		System.out.println("Edit " + reading);
	}
}