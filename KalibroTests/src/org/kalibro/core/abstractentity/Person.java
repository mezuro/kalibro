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
		setIdentityNumber(identityNumber);
		setName(name);
		setSex(sex);
		createRelatives();
		setRandom(Math.random());
	}

	private void createRelatives() {
		setRelatives(new TreeMap<String, Person>());
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public void addRelative(String relation, Person relative) {
		relatives.put(relation, relative);
	}

	public void setRelatives(Map<String, Person> relatives) {
		this.relatives = relatives;
	}

	public double getRandom() {
		return random;
	}

	public void setRandom(double random) {
		this.random = random;
	}
}