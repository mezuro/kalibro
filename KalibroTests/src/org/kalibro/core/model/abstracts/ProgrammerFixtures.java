package org.kalibro.core.model.abstracts;

import static org.kalibro.core.model.abstracts.PersonFixtures.*;

public class ProgrammerFixtures {

	public static Programmer programmerCarlos() {
		Programmer carlos = new Programmer(carlos(), true);
		carlos.addColleague(programmerPaulo());
		return carlos;
	}

	public static Programmer programmerPaulo() {
		return new Programmer(paulo(), true);
	}
}