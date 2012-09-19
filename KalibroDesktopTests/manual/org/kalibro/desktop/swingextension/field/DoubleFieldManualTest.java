package org.kalibro.desktop.swingextension.field;

public final class DoubleFieldManualTest extends NumberFieldManualTest<Double> {

	public static void main(String[] args) {
		new DoubleFieldManualTest().execute();
	}

	@Override
	protected String title() {
		return "DoubleField";
	}

	@Override
	protected NumberField<Double> normalField() {
		return new DoubleField("");
	}

	@Override
	protected NumberField<Double> specialNumberField() {
		return new DoubleField("", Double.POSITIVE_INFINITY);
	}
}