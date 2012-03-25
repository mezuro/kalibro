package org.kalibro.core.processing;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ScriptEvaluator {

	private String script;

	public ScriptEvaluator(String script) {
		this.script = script;
	}

	public Double invokeFunction(String functionName) throws Exception {
		ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
		scriptEngine.eval(script);
		Object returnValue = ((Invocable) scriptEngine).invokeFunction(functionName);
		return ((Number) returnValue).doubleValue();
	}
}