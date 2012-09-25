package org.kalibro.core.processing;

import org.kalibro.KalibroException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public final class JavascriptEvaluator {

	private Context context;
	private Scriptable script;

	public void addVariable(String name, Double value) {
		validateIdentifier(name);
		getScript().put(name, script, value);
	}

	public void addFunction(String name, String body) {
		validateIdentifier(name);
		Function function = getContext().compileFunction(script, "function(){\n" + body + "}", name, 0, null);
		script.put(name, script, function);
	}

	private void validateIdentifier(String identifier) {
		if (!identifier.matches("[a-zA-Z_$][a-zA-_$Z0-9]*"))
			throw new KalibroException("Invalid identifier: " + identifier);
	}

	public void remove(String name) {
		getScript().delete(name);
	}

	public Double evaluate(String name) {
		Object result = getScript().get(name, script);
		if (result instanceof Function)
			result = ((Function) result).call(context, script, null, null);
		return toDouble(result);
	}

	private Double toDouble(Object result) {
		return ((Number) result).doubleValue();
	}

	private Context getContext() {
		if (context == null)
			initialize();
		return context;
	}

	private Scriptable getScript() {
		if (script == null)
			initialize();
		return script;
	}

	private void initialize() {
		context = Context.enter();
		script = context.initStandardObjects();
	}

	@Override
	protected void finalize() throws Throwable {
		Context.exit();
		super.finalize();
	}
}