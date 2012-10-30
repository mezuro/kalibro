package org.kalibro.core.persistence;

/**
 * Utility query builder for {@link ProcessingDatabaseDao}.
 * 
 * @author Carlos Morais
 */
enum DatePicker {

	ANY(""), FIRST("min"), FIRST_AFTER("min", ">"), LAST("max"), LAST_BEFORE("max", "<");

	static final DatePicker AFTER = FIRST_AFTER;
	static final DatePicker BEFORE = LAST_BEFORE;

	private String selector;
	private String comparator;

	private DatePicker(String selector) {
		this(selector, null);
	}

	private DatePicker(String selector, String comparator) {
		this.selector = selector;
		this.comparator = comparator;
	}

	String existenceClause() {
		return "WHERE " + clause("processing");
	}

	String processingClause() {
		return processingClause("");
	}

	String processingClause(String extraCondition) {
		String dateCondition = "= (SELECT " + selector + "(p.date) FROM Processing p WHERE " + clause("p");
		dateCondition += extraCondition.equals("") ? ")" : " AND p." + extraCondition + ")";
		return clause("processing", dateCondition);
	}

	private String clause(String alias) {
		String dateCondition = comparator == null ? "" : comparator + " :date";
		return clause(alias, dateCondition);
	}

	private String clause(String alias, String dateCondition) {
		String dateClause = dateCondition.equals("") ? "" : " AND " + alias + ".date " + dateCondition;
		return alias + ".repository.id = :repositoryId" + dateClause;
	}
}