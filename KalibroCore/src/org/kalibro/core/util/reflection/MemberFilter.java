package org.kalibro.core.util.reflection;

import java.lang.reflect.Member;

/**
 * A filter for members in reflective operations.
 * 
 * @author Carlos Morais
 */
public interface MemberFilter {

	boolean accept(Member member);
}