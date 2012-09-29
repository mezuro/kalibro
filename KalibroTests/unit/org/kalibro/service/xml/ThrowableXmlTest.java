package org.kalibro.service.xml;

import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;

public class ThrowableXmlTest extends XmlTest<Throwable> {

	@Override
	protected Throwable loadFixture() {
		return new Throwable("ThrowableXmlTest message", new Throwable("ThrowableXmlTest cause message"));
	}

	@Test
	public void shouldThrowErrorIfThrowableIsNotConvertible() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				new ThrowableXml(new Throwable() {
					// Anonymous class
				}).convert();
			}
		}).throwsError().withMessage("Could not convert Error XML to Throwable").withCause(NullPointerException.class);
	}
}