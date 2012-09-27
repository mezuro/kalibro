package org.kalibro.core.processing;

import org.kalibro.KalibroException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public class JavascriptEvaluator {

	private Context context;
	private Scriptable script;

	public JavascriptEvaluator() {
		context = Context.enter();
		script = context.initStandardObjects();
	}

	public void addVariable(String name, Double value) {
		validateIdentifier(name);
		script.put(name, script, value);
	}

	public void addFunction(String name, String body) {
		validateIdentifier(name);
		script.put(name, script, compileFunction(name, body));
	}

	private void validateIdentifier(String identifier) {
		if (!identifier.matches("[a-zA-Z_$][a-zA-_$Z0-9]*"))
			throw new KalibroException("Invalid identifier: " + identifier);
	}

	private Function compileFunction(String name, String body) {
		try {
			return context.compileFunction(script, "function(){\n" + body + "}", name, 0, null);
		} catch (Exception exception) {
			throw new KalibroException("Error compiling Javascript for: " + name, exception);
		}
	}

	public Double evaluate(String name) {
		try {
			return doEvaluate(name);
		} catch (Exception exception) {
			throw new KalibroException("Error evaluating Javascript for: " + name, exception);
		}
	}

	private Double doEvaluate(String name) {
		Object result = script.get(name, script);
		if (result instanceof Function)
			result = ((Function) result).call(context, script, null, null);
		return ((Number) result).doubleValue();
	}

	public void close() {
		Context.exit();
	}
}