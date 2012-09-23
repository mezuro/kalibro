package org.kalibro;

import java.io.File;

import org.junit.rules.Timeout;
import org.kalibro.core.Environment;

public abstract class IntegrationTest extends TestCase {

	@Override
	protected Timeout testTimeout() {
		return new Timeout(8000);
	}

	protected File samplesDirectory() {
		return new File(Environment.dotKalibro(), "samples");
	}

	protected File repositoriesDirectory() {
		return new File(Environment.dotKalibro(), "repositories");
	}

	protected File helloWorldDirectory() {
		return new File(repositoriesDirectory(), "HelloWorld-1.0");
	}
}