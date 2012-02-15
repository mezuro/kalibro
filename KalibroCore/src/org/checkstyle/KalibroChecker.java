package org.checkstyle;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.api.AuditListener;

public class KalibroChecker {

	private Checker checker;

	public KalibroChecker(AuditListener listener) throws Exception {
		checker = new Checker();
		checker.setModuleClassLoader(Checker.class.getClassLoader());
		checker.addListener(listener);
		checker.configure(new CheckstyleConfiguration());
	}

	public void process(File codeDirectory) {
		List<File> files = new LinkedList<File>(FileUtils.listFiles(codeDirectory, new String[]{"java"}, true));
		checker.process(files);
		checker.destroy();
	}
}