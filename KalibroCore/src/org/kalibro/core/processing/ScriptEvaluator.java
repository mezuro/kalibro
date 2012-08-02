package org.kalibro.core.processing;

public interface ScriptEvaluator {

	void addVariable(String name, Double value);

	void addFunction(String name, String body);

	void remove(String name);

	Double evaluate(String name);
}