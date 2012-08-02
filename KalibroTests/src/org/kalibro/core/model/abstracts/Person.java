package org.kalibro.core.model.abstracts;

import java.util.Map;
import java.util.TreeMap;

@SortingMethods("getName")
public class Person extends AbstractEntity<Person> {

	@IdentityField
	private String identityNumber;

	private String name;
	private String sex;

	private Map<String, Person> relatives;

	@Ignore
	@SuppressWarnings("unused")
	private double toIgnore = Math.random();

	protected Person() {
		this("", "", "");
	}

	protected Person(String identityNumber, String name, String sex) {
		setIdentityNumber(identityNumber);
		setName(name);
		setSex(sex);
		createRelatives();
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
}