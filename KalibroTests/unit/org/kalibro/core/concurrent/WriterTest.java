package org.kalibro.core.concurrent;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class WriterTest extends UnitTest {

	private Object product;
	private Producer<Object> producer;

	private Writer<Object> writer;

	@Before
	public void setUp() {
		product = mock(Object.class);
		producer = mock(Producer.class);
		writer = new Writer<Object>(producer);
	}

	@Test
	public void shouldWriteObjectOnProducer() {
		writer.write(product);
		verify(producer).write(product);
	}

	@Test
	public void shouldNotifyProducerWhenClose() {
		writer.close();
		verify(producer).close(writer);
	}

	@Test
	public void shouldNotWriteIfClosed() {
		writer.close();
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				writer.write(product);
			}
		}).throwsException().withMessage("Writer is closed.");
	}
}