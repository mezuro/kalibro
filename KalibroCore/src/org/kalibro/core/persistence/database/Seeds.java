package org.kalibro.core.persistence.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.kalibro.core.persistence.database.entities.BaseToolRecord;
import org.kalibro.core.persistence.database.entities.ConfigurationRecord;
import org.kalibro.core.util.Directories;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

public class Seeds {

	private static final File SEEDED_FILE = new File(Directories.kalibro(), ".seeded");

	protected static void saveSeedsIfFirstTime(DatabaseManager databaseManager) {
		if (isFirstTime()) {
			saveSeeds(databaseManager);
			markAsSeeded();
		}
	}

	private static boolean isFirstTime() {
		return ! SEEDED_FILE.exists();
	}

	private static void saveSeeds(DatabaseManager databaseManager) {
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD);
		InputStream seedsStream = Seeds.class.getResourceAsStream("seeds.yml");
		Seeds seeds = (Seeds) yaml.load(seedsStream);
		databaseManager.save(seeds.baseToolSeeds);
		databaseManager.save(seeds.configurationSeeds);
	}

	private static void markAsSeeded() {
		try {
			SEEDED_FILE.createNewFile();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	protected List<BaseToolRecord> baseToolSeeds;
	protected List<ConfigurationRecord> configurationSeeds;

	public Seeds() {
		super();
	}
}