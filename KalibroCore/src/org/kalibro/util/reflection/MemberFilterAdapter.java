package org.kalibro.util.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;

/**
 * An abstract adapter class for filtering members. This class exists as convenience for creating member filters without
 * need for casting them to {@link AccessibleObject}.<br/>
 * The {@link MemberFilter} interface accepts instances of {@link Member}. All 3 known implementations of this interface
 * ({@link Constructor}, {@link Field} and {@link Method}) are subclasses of {@link AccessibleObject}.
 * 
 * @author Carlos Morais
 */
public abstract class MemberFilterAdapter implements MemberFilter {

	@Override
	public boolean accept(Member member) {
		return accept((AccessibleObject) member);
	}

	protected abstract boolean accept(AccessibleObject member);
}