package org.kalibro.desktop.swingextension.field;

public final class IntegerFieldManualTest extends NumberFieldManualTest<Integer> {

	public static void main(String[] args) {
		new IntegerFieldManualTest().execute();
	}

	@Override
	String title() {
		return "IntegerField";
	}

	@Override
	NumberField<Integer> createField() {
		return new IntegerField("");
	}
}