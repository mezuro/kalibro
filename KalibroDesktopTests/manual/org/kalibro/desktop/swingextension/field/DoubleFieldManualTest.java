package org.kalibro.desktop.swingextension.field;

public final class DoubleFieldManualTest extends NumberFieldManualTest<Double> {

	public static void main(String[] args) {
		new DoubleFieldManualTest().execute();
	}

	private boolean negative;

	@Override
	String title() {
		return "DoubleField";
	}

	@Override
	NumberField<Double> createField() {
		negative = !negative;
		return new DoubleField("", negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
	}
}