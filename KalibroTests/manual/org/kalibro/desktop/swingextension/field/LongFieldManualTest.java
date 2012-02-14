package org.kalibro.desktop.swingextension.field;

public class LongFieldManualTest extends NumberFieldManualTest<Long> {

	public static void main(String[] args) {
		new LongFieldManualTest().execute();
	}

	@Override
	protected String title() {
		return "LongField";
	}

	@Override
	protected NumberField<Long> normalField() {
		return new LongField("");
	}

	@Override
	protected NumberField<Long> specialNumberField() {
		return new LongField("", Long.MAX_VALUE);
	}
}