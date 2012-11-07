package org.kalibro.desktop.swingextension.dialog;

import java.awt.Component;
import java.awt.Window;

import org.kalibro.Language;
import org.kalibro.desktop.swingextension.panel.LanguagePanel;

public class LanguageDialog extends EditDialog<Language> {

	public LanguageDialog(Window owner) {
		super(owner, "Languages", new LanguagePanel());
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		super.createComponents(innerComponents);
	}
}