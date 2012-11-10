package org.kalibro.desktop.swingextension;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.kalibro.core.reflection.MethodReflector;

public class ReflectiveAction extends AbstractAction {

	private Object target;
	private String methodName;
	private Object[] arguments;

	private MethodReflector reflector;

	public ReflectiveAction(Object target, String methodName, Object... arguments) {
		this.target = target;
		this.methodName = methodName;
		this.arguments = arguments;
		validateMethod();
	}

	private void validateMethod() {
		reflector = new MethodReflector((target instanceof Class) ? (Class<?>) target : target.getClass());
		reflector.getReturnType(methodName, arguments);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		reflector.invoke(target, methodName, arguments);
	}
}