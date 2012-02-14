package org.kalibro.spago;

import java.io.StringReader;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilderFactory;

import org.kalibro.core.model.Project;
import org.kalibro.core.model.Repository;
import org.kalibro.core.model.enums.RepositoryType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class SpagoRequestParser {

	private Element root;
	private Project project;

	public SpagoRequestParser(String xmlRequest) throws Exception {
		InputSource inputSource = new InputSource(new StringReader(xmlRequest));
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource);
		root = (Element) document.getElementsByTagName("qualipso-spago4q").item(0);
		parseProject();
	}

	public Project getProject() {
		return project;
	}

	public boolean shouldIncludeProject() {
		return isLanguageAccepted() && isModelAccepted();
	}

	private boolean isLanguageAccepted() {
		return Arrays.asList("C", "C++").contains(getTextOnTag("language").toUpperCase());
	}

	private boolean isModelAccepted() {
		return containsModel("Kalibro") || containsModel("MOSST");
	}

	private boolean containsModel(String model) {
		return getTextOnTag("models").contains(model);
	}

	private void parseProject() {
		project = new Project();
		project.setName(parseProjectName());
		project.setLicense(getTextOnTag("license"));
		project.setRepository(parseRepository());
	}

	private String parseProjectName() {
		String name = getTextOnTag("project-name");
		String version = getTextOnTag("version");
		String release = getTextOnTag("release");
		return name + "-" + version + "R" + release;
	}

	private Repository parseRepository() {
		RepositoryType type = RepositoryType.SUBVERSION;
		String address = getTextOnTag("repository-path");
		String username = getTextOnTag("repository-username");
		String password = getTextOnTag("repository-password");
		return new Repository(type, address, username, password);
	}

	private String getTextOnTag(String element) {
		return ((Element) root.getElementsByTagName(element).item(0)).getTextContent();
	}
}