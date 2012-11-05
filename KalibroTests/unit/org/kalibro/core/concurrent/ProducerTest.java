package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.kalibro.tests.VoidAnswer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Producer.class)
public class ProducerTest extends UnitTest {

	private static final InterruptedException INTERRUPTION = new InterruptedException();
	private static final String INTERRUPTION_MESSAGE = "Producer interrupted while yielding.";

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

		producer.close(producer.createWriter());
		assertFalse(producer.canYield());
	}

	@Test
	public void canYieldShouldWaitWritersToCloseWhenQueueIsEmpty() throws InterruptedException {
		Writer<Object> writer = producer.createWriter();
		when(queue.isEmpty()).thenReturn(true);
		doAnswer(close(writer)).when(producer).wait();

		assertFalse(producer.canYield());
		verify(producer).wait();
	}

	private VoidAnswer close(final Writer<Object> writer) {
		return new VoidAnswer() {

			@Override
			public void answer() throws Throwable {
				producer.close(writer);
			}
		};
	}

	@Test
	public void canYieldShouldWaitWritersToWriteWhenQueueIsEmpty() throws InterruptedException {
		producer.createWriter();
		when(queue.isEmpty()).thenReturn(true);
		doAnswer(addSomethingToQueue()).when(producer).wait();

		assertTrue(producer.canYield());
		verify(producer).wait();
	}

	private VoidAnswer addSomethingToQueue() {
		return new VoidAnswer() {

			@Override
			public void answer() throws Throwable {
				when(queue.isEmpty()).thenReturn(false);
			}
		};
	}

	@Test
	public void shouldThrowExceptionIfCanYieldIsInterrupted() throws InterruptedException {
		producer.createWriter();
		when(queue.isEmpty()).thenReturn(true);
		doThrow(INTERRUPTION).when(producer).wait();
		verifyInterruption(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				producer.canYield();
			}
		});
	}

	@Test
	public void shouldYieldByTakingFromQueue() throws InterruptedException {
		when(queue.take()).thenReturn(product);
		assertSame(product, producer.yield());
	}

	@Test
	public void shouldThrowExceptionIfYieldIsInterrupted() throws InterruptedException {
		when(queue.take()).thenThrow(INTERRUPTION);
		verifyInterruption(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				producer.yield();
			}
		});
	}

	private void verifyInterruption(VoidTask task) {
		assertThat(task).throwsException().withMessage(INTERRUPTION_MESSAGE).withCause(INTERRUPTION);
	}

	@Test
	public void shouldIterateOnlyAfterStart() throws Exception {
		doAnswer(startProducer()).when(producer).wait();
		ProducerIterator<Object> iterator = mock(ProducerIterator.class);
		whenNew(ProducerIterator.class).withArguments(producer).thenReturn(iterator);

		assertSame(iterator, producer.iterator());
		verify(producer).wait();
	}

	private VoidAnswer startProducer() {
		return new VoidAnswer() {

			@Override
			public void answer() throws Throwable {
				Whitebox.setInternalState(producer, "started", true);
			}
		};
	}
}