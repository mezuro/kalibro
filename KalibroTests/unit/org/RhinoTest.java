package org;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public class RhinoTest {

	@Test
	public void test() {
		Context cx = Context.enter();
		Scriptable scope = cx.initStandardObjects();

		scope.put("x", scope, 6.0);
		scope.put("y", scope, 7.0);
		Function z = cx.compileFunction(scope, "function(){return x*y;}", "z", 0, null);
		Function bla = cx.compileFunction(scope, "function bla(){ return 'bla'+z(); }", "bla", 0, null);
		scope.put("bla", scope, bla);
		scope.put("z", scope, z);
		Object resultZ = z.call(cx, scope, null, null);
		Object resultBla = bla.call(cx, scope, null, null);

		assertEquals("bla42", resultBla);
		assertEquals(42.0, resultZ);
	}
}