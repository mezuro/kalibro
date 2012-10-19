package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Producer.class)
public class ProducerTest extends UnitTest {

	private Object product;
	private LinkedBlockingQueue<Object> queue;

	private Producer<Object> producer;

	@Before
	public void setUp() throws Exception {
		product = mock(Object.class);
		queue = mock(LinkedBlockingQueue.class);
		whenNew(LinkedBlockingQueue.class).withNoArguments().thenReturn(queue);
		producer = spy(new Producer<Object>());
	}

	@Test
	public void shouldWriteOnQueue() {
		producer.write(product);
		verify(queue).add(product);
	}

	@Test
	public void shouldNotifyOnWrite() {
		producer.write(product);
		verifyNotification();
	}

	@Test
	public void shouldNotifyOnClose() {
		producer.close(producer.createWriter());
		verifyNotification();
	}

	private void verifyNotification() {
		synchronized (producer) {
			verify(producer).notify();
		}
	}

	@Test
	public void canYieldIfQueueHasElements() {
		when(queue.isEmpty()).thenReturn(false);
		assertTrue(producer.canYield());
	}

	@Test
	public void cannotYieldIfQueueIsEmptyAndHasNoOpenWriters() {
		when(queue.isEmpty()).thenReturn(true);
		assertFalse(producer.canYield());

		Writer<Object> writer = producer.createWriter();
		producer.close(writer);
		assertFalse(producer.canYield());
	}

	@Test
	public void canYieldShouldWaitWritersToCloseWhenQueueIsEmpty() throws InterruptedException {
		final Writer<Object> writer = producer.createWriter();
		when(queue.isEmpty()).thenReturn(true);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				producer.close(writer);
				return null;
			}
		}).when(producer).wait();
		assertFalse(producer.canYield());
		verify(producer).wait();
	}

	@Test
	public void canYieldShouldWaitWritersToWriteWhenQueueIsEmpty() throws InterruptedException {
		producer.createWriter();
		when(queue.isEmpty()).thenReturn(true);
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				when(queue.isEmpty()).thenReturn(false);
				return null;
			}
		}).when(producer).wait();
		assertTrue(producer.canYield());
		verify(producer).wait();
	}

	@Test
	public void shouldThrowExceptionIfCanYieldIsInterrupted() throws InterruptedException {
		producer.createWriter();
		when(queue.isEmpty()).thenReturn(true);
		InterruptedException interruption = new InterruptedException();
		doThrow(interruption).when(producer).wait();
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				producer.canYield();
			}
		}).throwsException().withMessage("Producer interrupted while yielding.").withCause(interruption);
	}

	@Test
	public void shouldYieldByTakingFromQueue() throws InterruptedException {
		when(queue.take()).thenReturn(product);
		assertSame(product, producer.yield());
	}

	@Test
	public void shouldThrowExceptionIfYieldIsInterrupted() throws InterruptedException {
		InterruptedException interruption = new InterruptedException();
		when(queue.take()).thenThrow(interruption);
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				producer.yield();
			}
		}).throwsException().withMessage("Producer interrupted while yielding.").withCause(interruption);
	}

	@Test
	public void shouldIterateProperly() throws Exception {
		ProducerIterator<Object> iterator = mock(ProducerIterator.class);
		whenNew(ProducerIterator.class).withArguments(producer).thenReturn(iterator);
		assertSame(iterator, producer.iterator());
	}
}