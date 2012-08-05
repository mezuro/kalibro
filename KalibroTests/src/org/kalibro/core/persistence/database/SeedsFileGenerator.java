package org.kalibro.core.persistence.database;

import static org.kalibro.core.model.ConfigurationFixtures.*;

import java.util.Arrays;

import org.kalibro.core.persistence.database.entities.ConfigurationRecord;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

public class SeedsFileGenerator {

	public static void main(String[] args) {
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD);
		Seeds seeds = new SeedsFileGenerator().getSeeds();

		System.out.println(yaml.dump(seeds));
		System.exit(0);
	}

	public Seeds getSeeds() {
		Seeds seeds = new Seeds();
		seeds.configurationSeeds = Arrays.asList(new ConfigurationRecord(kalibroConfiguration()));
		return seeds;
	}
}