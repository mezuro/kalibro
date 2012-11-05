package org.kalibro.desktop.swingextension.field;

public final class IntegerFieldManualTest extends NumberFieldManualTest<Integer> {

	public static void main(String[] args) {
		new IntegerFieldManualTest().execute();
	}

	@Override
	protected String title() {
		return "IntegerField";
	}

	@Override
	protected NumberField<Integer> normalField() {
		return new IntegerField("");
	}

	@Override
	protected NumberField<Integer> specialNumberField() {
		return new IntegerField("", Integer.MAX_VALUE);
	}
}