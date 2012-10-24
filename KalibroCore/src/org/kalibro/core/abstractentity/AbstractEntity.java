package org.kalibro.core.abstractentity;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

import org.apache.commons.io.FileUtils;
import org.kalibro.KalibroException;
import org.kalibro.core.Identifier;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

/**
 * This abstract class provides common methods to minimize the effort required to create an entity.<br/>
 * - Overrides {@code hashCode()} and {@code equals()} based on identity fields (see {@link IdentityField}).<br/>
 * - Implements {@code Comparable.compareTo()} based on sorting fields (see {@link SortingFields}).<br/>
 * - Provides methods to import and export entities to files, printing them in YAML format.<br/>
 * - Overrides the default {@code toString()} with the YAML print.
 * 
 * @author Carlos Morais
 */
public abstract class AbstractEntity<T extends Comparable<? super T>> implements Comparable<T>, Serializable {

	protected static <T> T importFrom(File file, Class<T> entityType) {
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD);
		try {
			return yaml.loadAs(new FileInputStream(file), entityType);
		} catch (Exception exception) {
			throw new KalibroException("Could not import " + entityName(entityType) + " from file: " + file, exception);
		}
	}

	private static String entityName(Class<?> entityType) {
		String className = entityType.getSimpleName();
		return Identifier.fromVariable(className).asText().toLowerCase();
	}

	public void exportTo(File file) {
		try {
			FileUtils.writeStringToFile(file, print());
		} catch (Exception exception) {
			throw new KalibroException("Could not export " + entityName(getClass()) + " to file: " + file, exception);
		}
	}

	@Override
	public String toString() {
		return print();
	}

	private String print() {
		return Printer.print(this);
	}

	@Override
	public int hashCode() {
		return HashCodeCalculator.hash(this);
	}

	@Override
	public boolean equals(Object other) {
		return Equality.areEqual(this, other);
	}

	public boolean deepEquals(Object other) {
		return Equality.areDeepEqual(this, other);
	}

	@Override
	public int compareTo(T other) {
		return new EntityComparator<T>().compare(this, other);
	}

	protected void throwExceptionIf(boolean condition, String message) {
		if (condition)
			throw new KalibroException(message);
	}
}