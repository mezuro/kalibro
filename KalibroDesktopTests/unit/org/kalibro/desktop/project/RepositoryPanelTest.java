package org.kalibro.desktop.project;

import static org.junit.Assert.*;
import static org.kalibro.RepositoryFixtures.newHelloWorldRepository;
import static org.kalibro.RepositoryType.*;

import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.RepositoryDao;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.desktop.swingextension.field.PasswordField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareForTest(DaoFactory.class)
public class RepositoryPanelTest extends UnitTest {

	private Repository repository;

	private RepositoryPanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		repository = newHelloWorldRepository(SUBVERSION);
		repository.setUsername("RepositoryPanelTest username");
		repository.setPassword("RepositoryPanelTest password");
		mockDaoFactory();
		panel = new RepositoryPanel();
		finder = new ComponentFinder(panel);
	}

	private void mockDaoFactory() {
		RepositoryDao dao = mock(RepositoryDao.class);
		TreeSet<RepositoryType> types = new TreeSet<RepositoryType>(asList(RepositoryType.values()));
		mockStatic(DaoFactory.class);
		when(DaoFactory.getRepositoryDao()).thenReturn(dao);
		when(dao.supportedTypes()).thenReturn(types);
	}

	@Test
	public void shouldGet() {
		typeField().set(repository.getType());
		stringField("address").set(repository.getAddress());
		stringField("username").set(repository.getUsername());
		passwordField().set(repository.getPassword());
		assertDeepEquals(repository, panel.get());
	}

	@Test
	public void shouldSet() {
		panel.set(repository);
		assertEquals(repository.getType(), typeField().get());
		assertEquals(repository.getAddress(), stringField("address").get());
		assertEquals(repository.getUsername(), stringField("username").get());
		assertEquals(repository.getPassword(), passwordField().get());
	}

	@Test
	public void authenticationFieldsShouldBeEnabledOnlyIfAuthenticationIsSupported() {
		panel.set(repository);

		typeField().set(GIT);
		assertFalse(stringField("username").isEnabled());
		assertFalse(passwordField().isEnabled());
		assertEquals("", stringField("username").get());
		assertEquals("", passwordField().get());

		typeField().set(SUBVERSION);
		assertTrue(stringField("username").isEnabled());
		assertTrue(passwordField().isEnabled());
	}

	private ChoiceField<RepositoryType> typeField() {
		return finder.find("type", ChoiceField.class);
	}

	private StringField stringField(String name) {
		return finder.find(name, StringField.class);
	}

	private PasswordField passwordField() {
		return finder.find("password", PasswordField.class);
	}
}