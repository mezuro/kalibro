package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.client.EndpointPortFactory;
import org.kalibro.core.model.enums.RepositoryType;

public class KalibroEndpointTest extends KalibroServiceTestCase {

	private KalibroEndpoint endpoint;

	@Before
	public void setUp() {
		endpoint = EndpointPortFactory.getEndpointPort(KalibroEndpoint.class);
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT)
	public void shouldProvideOnlyRemoteRepositoryTypesAsSupported() {
		Set<RepositoryType> repositoryTypes = endpoint.getSupportedRepositoryTypes();
		for (RepositoryType type : RepositoryType.values())
			assertEquals(type.toString(), !type.isLocal(), repositoryTypes.contains(type));
	}
}