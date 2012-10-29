package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

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
		assertEquals(tableName(), table.name());
	}

	private String tableName() {
		return "\"" + Identifier.fromVariable(entityName()).asConstant() + "\"";
	}

	@Test
	public void shouldHaveCorrectColumns() {
		verifyColumns();
	}

	protected abstract void verifyColumns();

	protected void shouldHaveId() {
		assertColumn("id", Long.class).isRequired().isNotUnique();
		annotation("id", Id.class);
		annotation("id", GeneratedValue.class);
	}

	protected ColumnMatcher assertColumn(String field, Class<?> type) {
		assertFieldType(field, type);
		return new ColumnMatcher(annotation(field, Column.class)).named(columnName(field));
	}

	protected OneToManyMatcher assertOneToMany(String field) {
		assertFieldType(field, Collection.class);
		return new OneToManyMatcher(annotation(field, OneToMany.class)).isLazy().removeOrphans();
	}

	protected void assertOrderedElementCollection(String field) {
		assertOrdered(field);
		annotation(field, ElementCollection.class);
	}

	protected OneToManyMatcher assertOrderedOneToMany(String field) {
		assertOrdered(field);
		return new OneToManyMatcher(annotation(field, OneToMany.class)).isLazy().removeOrphans().cascades();
	}

	private void assertOrdered(String field) {
		assertFieldType(field, List.class);
		OrderColumn orderColumn = annotation(field, OrderColumn.class);
		assertEquals("Wrong @OrderColumn name.", "\"index\"", orderColumn.name());
		assertFalse("@OrderColumn should NOT be nullable.", orderColumn.nullable());
	}

	protected ManyToOneMatcher assertManyToOne(String field, Class<?> type) {
		assertFieldType(field, type);
		return new ManyToOneMatcher(annotation(field, ManyToOne.class), joinColumn(field)).doesNotCascade().isLazy();
	}

	protected void shouldHaveError(String field) {
		assertFieldType(field, ThrowableRecord.class);
		new OneToOneMatcher(annotation(field, OneToOne.class), joinColumn(field)).cascades().isEager().isOptional();
	}

	private JoinColumn joinColumn(String field) {
		JoinColumn joinColumn = annotation(field, JoinColumn.class);
		assertEquals("Wrong @JoinColumn name.", columnName(field), joinColumn.name());
		assertEquals("@JoinColumn " + joinColumn.name() + " references wrong column.",
			"\"id\"", joinColumn.referencedColumnName());
		return joinColumn;
	}

	private void assertFieldType(String field, Class<?> type) {
		assertTrue("Field not present: " + field, dtoReflector.listFields().contains(field));
		assertEquals("Wrong type for field " + field + ".", type, dtoReflector.getFieldType(field));
	}

	private <T extends Annotation> T annotation(String field, Class<T> annotationClass) {
		T annotation = dtoReflector.getFieldAnnotation(field, annotationClass);
		assertNotNull("@" + annotationClass.getSimpleName() + " not present for field: " + field, annotation);
		return annotation;
	}

	private String columnName(String field) {
		return "\"" + Identifier.fromVariable(field).asConstant().toLowerCase() + "\"";
	}
}