package org.kalibro.core.model.abstracts;

import static org.kalibro.core.model.abstracts.PersonFixtures.*;

class ProgrammerFixtures {

	protected static Programmer programmerCarlos() {
		Programmer carlos = new Programmer(carlos(), true);
		carlos.addColleague(programmerPaulo());
		return carlos;
	}

	protected static Programmer programmerPaulo() {
		return new Programmer(paulo(), true);
	}
}