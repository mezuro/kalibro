package org.kalibro.core.abstractentity;

import static org.kalibro.core.abstractentity.PersonFixtures.*;

final class ProgrammerFixtures {

	protected static Programmer programmerCarlos() {
		Programmer carlos = new Programmer(carlos(), true);
		carlos.addColleague(programmerPaulo());
		return carlos;
	}

	protected static Programmer programmerPaulo() {
		return new Programmer(paulo(), true);
	}

	private ProgrammerFixtures() {
		return;
	}
}