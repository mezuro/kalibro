package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.Collection;

import javax.persistence.*;

import org.junit.Test;
import org.kalibro.core.Identifier;
import org.kalibro.dto.ConcreteDtoTest;

public abstract class RecordTest extends ConcreteDtoTest {

	@Test
	public void persistenceEntityNameShouldBeEqualEntityName() throws ClassNotFoundException {
		Entity entityAnnotation = dtoClass().getAnnotation(Entity.class);
		assertNotNull("@Entity not present", entityAnnotation);
		assertEquals(entityName(), entityAnnotation.name());
	}

	@Test
	public void tableNameShouldBeEntityNameAsConstant() throws ClassNotFoundException {
		Table table = dtoClass().getAnnotation(Table.class);
		assertNotNull("@Table not present", table);
		assertEquals(databaseName(entityName()), table.name());
	}

	@Test
	public void shouldHaveCorrectColumns() {
		verifyColumns();
	}

	protected abstract void verifyColumns();

	protected void shouldHaveId() {
		annotation("id", Id.class);
		annotation("id", GeneratedValue.class);
		assertColumn("id", Long.class).isRequired().isUnique();
	}

	protected ColumnMatcher assertColumn(String field, Class<?> type) {
		assertFieldType(field, type);
		return new ColumnMatcher(annotation(field, Column.class)).named(databaseName(field));
	}

	protected OneToManyMatcher assertOneToMany(String field) {
		assertFieldType(field, Collection.class);
		return new OneToManyMatcher(annotation(field, OneToMany.class)).cascades();
	}

	protected ManyToOneMatcher assertManyToOne(String field, Class<?> type) {
		assertFieldType(field, type);
		return new ManyToOneMatcher(annotation(field, ManyToOne.class), joinColumn(field)).doesNotCascade();
	}

	protected OneToOneMatcher assertOneToOne(String field, Class<?> type) {
		assertFieldType(field, type);
		return new OneToOneMatcher(annotation(field, OneToOne.class), joinColumn(field)).cascades();
	}

	protected JoinColumn joinColumn(String field) {
		JoinColumn joinColumn = annotation(field, JoinColumn.class);
		assertEquals("Wrong @JoinColumn name.", databaseName(field), joinColumn.name());
		assertEquals("@JoinColumn " + joinColumn.name() + " references wrong column.",
			"\"id\"", joinColumn.referencedColumnName());
		return joinColumn;
	}

	protected void assertFieldType(String field, Class<?> type) {
		assertTrue("Field not present: " + field, dtoReflector.listFields().contains(field));
		assertEquals("Wrong type for field " + field + ".", type, dtoReflector.getFieldType(field));
	}

	protected <T extends Annotation> T annotation(String field, Class<T> annotationClass) {
		T annotation = dtoReflector.getFieldAnnotation(field, annotationClass);
		assertNotNull("@" + annotationClass.getSimpleName() + " not present for field: " + field, annotation);
		return annotation;
	}

	private String databaseName(String identifier) {
		return "\"" + Identifier.fromVariable(identifier).asConstant().toLowerCase() + "\"";
	}
}