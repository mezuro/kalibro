package org.kalibro.desktop.swingextension.panel;

import org.kalibro.core.model.enums.Language;

public class LanguagePanelStub extends EditPanel<Language> {

	public LanguagePanelStub() {
		super("language");
	}

	@Override
	protected void createComponents() {
		return;
	}

	@Override
	protected void buildPanel() {
		return;
	}

	@Override
	public Language retrieve() {
		return null;
	}

	@Override
	public void show(Language language) {
		return;
	}
}