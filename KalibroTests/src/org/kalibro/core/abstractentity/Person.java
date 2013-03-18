package org.kalibro.core.abstractentity;

import java.util.Map;
import java.util.TreeMap;

@SortingFields("name")
class Person extends AbstractEntity<Person> {

	@IdentityField
	private String identityNumber;

	@Print(order = 1, comment = "name comes first")
	private String name;

	private String sex;

	private Map<String, Person> relatives;

	@Ignore
	private double random;

	protected Person() {
		this("", "", "");
	}

	protected Person(String identityNumber, String name, String sex) {
		this.identityNumber = identityNumber;
		this.name = name;
		setSex(sex);
		relatives = new TreeMap<String, Person>();
		random = Math.random();
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public String getName() {
		return name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Map<String, Person> getRelatives() {
		return relatives;
	}
}