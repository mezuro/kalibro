package org.kalibro.core.util.reflection;

import java.lang.reflect.Member;

public interface MemberFilter {

	boolean accept(Member member);
}