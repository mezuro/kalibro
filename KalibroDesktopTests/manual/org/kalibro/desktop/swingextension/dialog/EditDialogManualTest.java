package org.kalibro.desktop.swingextension.dialog;

import org.kalibro.desktop.swingextension.field.PalindromeField;

public final class EditDialogManualTest extends EditDialog<String> implements EditDialogListener<String> {

	public static void main(String[] args) {
		new EditDialogManualTest().setVisible(true);
	}

	private EditDialogManualTest() {
		super(null, "EditDialog", new PalindromeField());
		addListener(this);
	}

	@Override
	public boolean dialogConfirm(String value) {
		System.out.println(value);
		boolean exceeded = value.length() > 15;
		if (exceeded)
			new ErrorDialog(this).show(new Exception("Maximum length is 15"));
		return !exceeded;
	}
}