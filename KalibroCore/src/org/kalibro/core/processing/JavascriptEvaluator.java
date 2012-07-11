package org.kalibro.core.processing;

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
		script.put(name, script, value);
	}

	public void addFunction(String name, String body) {
		Function function = context.compileFunction(script, "function(){\n" + body + "}", name, 0, null);
		script.put(name, script, function);
	}

	public Double evaluate(String name) {
		Object result = script.get(name, script);
		if (result instanceof Function)
			result = ((Function) result).call(context, script, null, null);
		return toDouble(result);
	}

	private Double toDouble(Object result) {
		return ((Number) result).doubleValue();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Context.exit();
	}
}