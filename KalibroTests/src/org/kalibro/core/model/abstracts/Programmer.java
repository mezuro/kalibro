package org.kalibro.core.model.abstracts;

import java.util.ArrayList;
import java.util.List;

@SortingMethods({"getUseMetrics", "getName"})
public class Programmer extends Person {

	private Boolean useMetrics;

	private List<Programmer> colleagues;

	protected Programmer(Person person, Boolean useMetrics) {
		super(person.getIdentityNumber(), person.getName(), person.getSex());
		this.useMetrics = useMetrics;
		colleagues = new ArrayList<Programmer>();
	}

	public Boolean getUseMetrics() {
		return useMetrics;
	}

	public void setUseMetrics(Boolean useMetrics) {
		this.useMetrics = useMetrics;
	}

	public List<Programmer> getColleagues() {
		return colleagues;
	}

	public void addColleague(Programmer colleague) {
		colleagues.add(colleague);
	}
}