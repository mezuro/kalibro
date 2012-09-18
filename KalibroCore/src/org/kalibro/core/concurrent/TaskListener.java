package org.kalibro.core.concurrent;

/**
 * The listener interface for tasks. The class that is interested in a task implements this interface, and the object
 * created with that class is registered using the {@link Task}{@code .addListener} method. When the task finishes, that
 * object's {@code taskFinished} method is invoked.
 * 
 * @author Carlos Morais
 */
public interface TaskListener {

	void taskFinished(TaskReport report);
}