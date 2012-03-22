package org.kalibro.core.persistence.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.analizo.AnalizoMetricCollector;
import org.checkstyle.CheckstyleMetricCollector;
import org.kalibro.core.MetricCollector;
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
		List<BaseToolRecord> baseTools = new ArrayList<BaseToolRecord>();
		baseTools.add(newBaseTool("Analizo", AnalizoMetricCollector.class));
		baseTools.add(newBaseTool("Checkstyle", CheckstyleMetricCollector.class));
		return baseTools;
	}

	private BaseToolRecord newBaseTool(String name, Class<? extends MetricCollector> collectorClass) {
		BaseTool baseTool = new BaseTool(name);
		baseTool.setCollectorClass(collectorClass);
		return new BaseToolRecord(baseTool);
	}

	private List<ConfigurationRecord> configurationSeeds() {
		Configuration configuration = ConfigurationFixtures.kalibroConfiguration();
		return Arrays.asList(new ConfigurationRecord(configuration));
	}
}