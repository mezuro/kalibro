package org.kalibro.desktop.swingextension.field;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;

import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public abstract class NumberField<T extends Number> extends EditPanel<T> implements Field<T> {

	private JFormattedTextField valueField;

	private GridBagPanelBuilder builder;

	public NumberField(String name) {
		super(name);
	}

	public NumberField(String name, T specialNumber) {
		super(name);
		addSpecialNumberButton(specialNumber);
	}

	@Override
	protected void createComponents() {
		valueField = new JFormattedTextField(new NumberFormatter(getDecimalFormat()));
		valueField.setName(getName());
		valueField.setSize(new FieldSize(valueField));
		valueField.setHorizontalAlignment(SwingConstants.RIGHT);
		valueField.setColumns(getColumns());
	}

	public abstract DecimalFormat getDecimalFormat();

	protected abstract int getColumns();

	@Override
	protected void buildPanel() {
		builder = new GridBagPanelBuilder(this, 0);
		builder.add(valueField, 1.0);
	}

	private void addSpecialNumberButton(final T specialNumber) {
		String buttonTitle = getDecimalFormat().format(specialNumber);
		Button specialNumberButton = new Button(getName(), buttonTitle, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				setValue(specialNumber);
			}
		});
		builder.add(new Label("  "));
		builder.add(specialNumberButton);
	}

	@Override
	public void show(T value) {
		setValue(value);
	}

	@Override
	public void setValue(T value) {
		valueField.setValue(value);
	}

	@Override
	public T retrieve() {
		return getValue();
	}

	@Override
	public T getValue() {
		Number value = (Number) valueField.getValue();
		return (value == null) ? null : parseValue(value);
	}

	protected abstract T parseValue(Number value);

	public Border getTextBorder() {
		return valueField.getBorder();
	}

	public void setTextBorder(Border border) {
		valueField.setBorder(border);
	}
}