package org.zcode.generator.utilities;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleTestingPath {
	
	public static void main(String[] args) {
		Path path = Paths.get("");
		String directoryName = path.toAbsolutePath().toString();
		System.out.println("Current Working Directory is = " +directoryName);
	}

}
