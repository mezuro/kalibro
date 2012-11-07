package org.kalibro.core.persistence.record;

import org.junit.runner.RunWith;
import org.kalibro.Configuration;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dto.DaoLazyLoader;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoLazyLoader.class)
public class RepositoryRecordTest extends RecordTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		Long id = Whitebox.getInternalState(entity, "id");
		Configuration configuration = Whitebox.getInternalState(entity, "configuration");
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(ConfigurationDao.class, "configurationOf", id)).thenReturn(configuration);
	}

	@Override
	protected void verifyColumns() {
		assertManyToOne("project", ProjectRecord.class).isRequired();
		shouldHaveId();
		assertColumn("name", String.class).isRequired();
		assertColumn("type", String.class).isRequired();
		assertColumn("address", String.class).isRequired();
		assertColumn("description", String.class).isNullable();
		assertColumn("license", String.class).isNullable();
		assertColumn("processPeriod", Integer.class).isNullable();
		assertManyToOne("configuration", ConfigurationRecord.class).isRequired();
		assertOneToMany("processings").doesNotCascade().isMappedBy("repository");
	}
}