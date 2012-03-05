package org.checkstyle;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.Configuration;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class KalibroChecker {

	private Checker checker;

	public KalibroChecker(AuditListener listener, Configuration configuration) throws Exception {
		checker = new Checker();
		checker.setModuleClassLoader(Checker.class.getClassLoader());
		checker.addListener(listener);
		checker.configure(configuration);
	}

	public void process(File codeDirectory) {
		List<File> files = new LinkedList<File>(FileUtils.listFiles(codeDirectory, new String[]{"java"}, true));
		checker.process(files);
		checker.destroy();
	}
}