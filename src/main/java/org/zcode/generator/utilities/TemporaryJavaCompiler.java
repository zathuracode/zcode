package org.zcode.generator.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

/**
 * Compiles generated temporary sources in-process so metadata can be loaded
 * without relying on a legacy Ant javac step or local jar files.
 */
public final class TemporaryJavaCompiler {

	private TemporaryJavaCompiler() {
	}

	public static void compileSources(Path sourceRoot) throws IOException {
		if (Files.notExists(sourceRoot)) {
			return;
		}

		List<File> sourceFiles = Files.walk(sourceRoot)
				.filter(path -> Files.isRegularFile(path) && path.toString().endsWith(".java"))
				.map(Path::toFile)
				.toList();

		if (sourceFiles.isEmpty()) {
			return;
		}

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			throw new IllegalStateException("No system Java compiler available to compile temporary generated sources");
		}

		try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8)) {
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT, List.of(sourceRoot.toFile()));

			List<String> options = new ArrayList<>();
			options.add("-classpath");
			options.add(System.getProperty("java.class.path"));
			options.add("--release");
			options.add(Integer.toString(Runtime.version().feature()));
			options.add("-proc:none");

			boolean success = compiler.getTask(null, fileManager, null, options, null,
					fileManager.getJavaFileObjectsFromFiles(sourceFiles)).call();
			if (!success) {
				throw new IllegalStateException("Failed to compile temporary generated sources in " + sourceRoot);
			}
		}
	}
}
