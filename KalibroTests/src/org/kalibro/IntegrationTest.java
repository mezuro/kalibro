package org.kalibro;

import java.io.File;

import org.kalibro.core.Environment;

public class IntegrationTest extends TestCase {

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