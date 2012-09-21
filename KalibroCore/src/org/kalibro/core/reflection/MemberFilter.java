package org.kalibro.core.reflection;

import java.lang.reflect.Member;

/**
 * A filter for members for used on reflective operations.
 * 
 * @author Carlos Morais
 */
public interface MemberFilter {

	boolean accept(Member member);
}