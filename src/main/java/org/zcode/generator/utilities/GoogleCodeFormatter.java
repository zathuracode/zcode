package org.zcode.generator.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 2.0
 */
@Slf4j
public class GoogleCodeFormatter {

	

	/**
	 * The Constructor.
	 */
	private GoogleCodeFormatter() {}
	
	public static void formatJavaCodeFile(String pathFiles) {
		
		log.info("GoogleCodeFormatter Java in file:"+pathFiles);
		
		try {
			
			if (pathFiles.endsWith(".java")) {
				runFormatterProcess(new File(pathFiles));
			}
			
		} catch (Throwable t) {
			log.warn("Skipping format for {} due to formatter runtime issue: {}", pathFiles, t.toString());
		}
		
	}

	private static void runFormatterProcess(File file) throws IOException, InterruptedException {
		String javaHome = System.getProperty("java.home");
		String javaBinary = javaHome + File.separator + "bin" + File.separator + "java";
		String classPath = System.getProperty("java.class.path");

		List<String> command = new ArrayList<>();
		command.add(javaBinary);
		command.add("--add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED");
		command.add("--add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED");
		command.add("--add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED");
		command.add("--add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED");
		command.add("--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED");
		command.add("--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED");
		command.add("-cp");
		command.add(classPath);
		command.add("com.google.googlejavaformat.java.Main");
		command.add("--replace");
		command.add(file.getAbsolutePath());

		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.redirectErrorStream(true);

		Process process = processBuilder.start();
		String formatterOutput = new String(process.getInputStream().readAllBytes());
		int exitCode = process.waitFor();

		if (exitCode != 0) {
			log.warn("Formatter exited with code {} for {}: {}", exitCode, file.getAbsolutePath(), formatterOutput.trim());
		}
	}
	

}
