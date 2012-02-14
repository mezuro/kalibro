package org.kalibro.desktop.old.listeners;

import org.kalibro.core.model.ModuleResult;

public interface ResultPanelListener {

	public void chooseConfiguration();

	public void exportResults(ModuleResult moduleResult);

	public void requestAnalysis();
}