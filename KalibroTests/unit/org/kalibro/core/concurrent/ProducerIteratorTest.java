package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class ProducerIteratorTest extends UnitTest {

	private Producer<Object> producer;
	private ProducerIterator<Object> iterator;

	@Before
	public void setUp() {
		producer = mock(Producer.class);
		iterator = new ProducerIterator<Object>(producer);
	}

	@Test
	public void shouldAnswerIfHasNext() {
		assertFalse(iterator.hasNext());
		when(producer.canYield()).thenReturn(true);
		assertTrue(iterator.hasNext());
	}

	@Test
	public void shouldGetNext() {
		Object product = mock(Object.class);
		when(producer.yield()).thenReturn(product);
		assertSame(product, iterator.next());
	}

	@Test
	public void shouldNotSupportRemoval() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				iterator.remove();
			}
		}).doThrow(UnsupportedOperationException.class).withMessage("Cannot remove from producer.");
	}
}