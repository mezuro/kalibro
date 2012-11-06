package org.kalibro.desktop.swingextension.panel;

import java.awt.Component;
import java.awt.GridLayout;

import org.kalibro.Language;
import org.kalibro.desktop.swingextension.field.ChoiceField;

class LanguagePanel extends EditPanel<Language> {

	private ChoiceField<Language> field;

	LanguagePanel() {
		super("language");
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		field = new ChoiceField<Language>("language", Language.values());
	}

	@Override
	protected void buildPanel() {
		setLayout(new GridLayout());
		add(field);
	}

	@Override
	public Language get() {
		return field.get();
	}

	@Override
	public void set(Language language) {
		field.set(language);
	}
}