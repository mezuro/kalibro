package org.kalibro;

import org.hamcrest.Matcher;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.powermock.api.mockito.PowerMockito;

public abstract class MockitoProxy extends PowerMockito {

	public static <T> T verify(T mock) {
		return Mockito.verify(mock);
	}

	public static <T> T verify(T mock, VerificationMode mode) {
		return Mockito.verify(mock, mode);
	}

	public static VerificationMode never() {
		return Mockito.never();
	}

	public static VerificationMode once() {
		return Mockito.times(1);
	}

	public static VerificationMode times(int times) {
		return Mockito.times(times);
	}

	public static <T> T any() {
		return Matchers.any();
	}

	public static String anyString() {
		return Matchers.anyString();
	}

	public static <T> T any(Class<T> type) {
		return Matchers.any(type);
	}

	public static <T> T eq(T expected) {
		return Matchers.eq(expected);
	}

	public static <T> T same(T expected) {
		return Matchers.same(expected);
	}

	public static <T> T argThat(Matcher<T> matcher) {
		return Matchers.argThat(matcher);
	}
}