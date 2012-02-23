package org.kalibro.core.persistence.database;

import java.util.Arrays;
import java.util.List;

import org.analizo.AnalizoMetricCollector;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.ConfigurationFixtures;
import org.kalibro.core.persistence.database.entities.BaseToolRecord;
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
		seeds.baseToolSeeds = baseToolSeeds();
		seeds.configurationSeeds = configurationSeeds();
		return seeds;
	}

	private List<BaseToolRecord> baseToolSeeds() {
		BaseTool analizo = new BaseTool("Analizo");
		analizo.setCollectorClass(AnalizoMetricCollector.class);
		return Arrays.asList(new BaseToolRecord(analizo));
	}

	private List<ConfigurationRecord> configurationSeeds() {
		Configuration configuration = ConfigurationFixtures.kalibroConfiguration();
		return Arrays.asList(new ConfigurationRecord(configuration));
	}
}