package org.kalibro;

public interface Observer <Subject, Interest> {

	void update(Subject subject, Interest interest);
}
