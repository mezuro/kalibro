package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

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
		assertNotNull("@Id not present", dtoReflector.getFieldAnnotation("id", Id.class));
		assertNotNull("@GeneratedValue not present", dtoReflector.getFieldAnnotation("id", GeneratedValue.class));
		assertColumn("id", Long.class).isRequired().isNotUnique();
	}

	protected ColumnMatcher assertColumn(String field, Class<?> type) {
		assertEquals(type, dtoReflector.getFieldType(field));
		Column column = dtoReflector.getFieldAnnotation(field, Column.class);
		assertNotNull("@Column not present for field: " + field, column);
		assertEquals(columnName(field), column.name());
		return new ColumnMatcher(column);
	}

	protected OneToManyMatcher assertOneToMany(String field) {
		assertTrue(Collection.class.isAssignableFrom(dtoReflector.getFieldType(field)));

		OneToMany oneToMany = dtoReflector.getFieldAnnotation(field, OneToMany.class);
		assertNotNull("@OneToMany not present for field: " + field, oneToMany);
		assertArrayEquals(new CascadeType[]{CascadeType.ALL}, oneToMany.cascade());
		assertEquals(FetchType.LAZY, oneToMany.fetch());
		assertTrue("Orphan removal should be true for " + field, oneToMany.orphanRemoval());
		return new OneToManyMatcher(oneToMany);
	}

	protected ManyToOneMatcher assertManyToOne(String field, Class<?> type) {
		assertEquals(type, dtoReflector.getFieldType(field));

		ManyToOne manyToOne = dtoReflector.getFieldAnnotation(field, ManyToOne.class);
		assertNotNull("@ManyToOne not present for field: " + field, manyToOne);
		assertEquals(0, manyToOne.cascade().length);
		assertEquals(FetchType.LAZY, manyToOne.fetch());

		JoinColumn joinColumn = dtoReflector.getFieldAnnotation(field, JoinColumn.class);
		assertNotNull("@JoinColumn not present for field: " + field, joinColumn);
		assertEquals(columnName(field), joinColumn.name());
		assertEquals("@JoinColumn should reference \"id\"", "\"id\"", joinColumn.referencedColumnName());

		return new ManyToOneMatcher(manyToOne, joinColumn);
	}

	private String columnName(String field) {
		return "\"" + Identifier.fromVariable(field).asConstant().toLowerCase() + "\"";
	}
}