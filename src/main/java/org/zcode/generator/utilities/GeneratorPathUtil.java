/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zcode.generator.utilities;

import java.io.File;
import java.util.List;

/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public class GeneratorPathUtil {

	
	/** The project name. */
	public static String projectName;
	
	/** The java entity package. */
	public static String javaEntityPackage;

	/** The workspace folder path. */
	public static String workspaceFolderPath;
	
	/** The java class folder path. */
	public static String javaClassFolderPath;
	
	/** The java source folder path. */
	public static String javaSourceFolderPath;
	
	/** The full path project. */
	public static String fullPathProject;

	/** The zathura generator name. */
	public static String zathuraGeneratorName;
	
	/** The meta data reader. */
	public static int metaDataReader;

	/** The connection driver class. */
	public static String connectionDriverClass;
	
	/** The connection driver name. */
	public static String connectionDriverName;
	
	/** The connection url. */
	public static String connectionUrl;
	
	/** The connection username. */
	public static String connectionUsername;
	
	/** The connection password. */
	public static String connectionPassword;
	
	/** The connection driver template. */
	public static String connectionDriverTemplate;
	
	
	public static String connectionGroupId;
	
	public static String connectionArtifactId;
	
	public static String connectionVersion;
	
	/** The company domain name. */
	public static String companyDomainName;
	
	/** The catalog. */
	public static String catalog;
	
	/** The schema. */
	public static String schema;
	
	/** The catalog and schema. */
	public static String catalogAndSchema;
	
	/** The tables list. */
	public static List<String> tablesList;
	
	public static File pomXmlFile;
	
	public static String groupIdMavenPoject;
	
	public static String artifactIdMavenProject;

	/**
	 * Reset.
	 */
	public static void reset() {
		//project = null;
		projectName = null;

		javaEntityPackage = null;

		workspaceFolderPath = null;
		javaClassFolderPath = null;
		javaSourceFolderPath = null;
		
		fullPathProject = null;

		zathuraGeneratorName = null;
		metaDataReader = 0;

		connectionDriverClass = null;
		connectionUrl = null;
		catalog = null;
		connectionUsername = null;
		connectionPassword = null;
		companyDomainName = null;
		
		schema = null;
		catalogAndSchema = null;
		tablesList = null;
		
		pomXmlFile = null;
		groupIdMavenPoject = null;
		artifactIdMavenProject = null;
	}

	

	/**
	 * Validar package.
	 *
	 * @param packageName the package name
	 * @throws Exception the exception
	 */
	public static void validarPackage(String packageName) throws Exception {
		if (packageName.startsWith(".") || packageName.endsWith(".")) {
			throw new Exception("A package name cannot start or end with a dot");
		}
	}	

	/**
	 * Replace all.
	 *
	 * @param cadena the cadena
	 * @param old the old
	 * @param snew the snew
	 * @return the string
	 */
	public static String replaceAll(String cadena, String old, String snew) {
		StringBuffer replace = new StringBuffer();
		String aux;

		for (int i = 0; i < cadena.length(); i++) {
			if ((i + old.length()) < cadena.length()) {
				aux = cadena.substring(i, i + old.length());
				if (aux.equals(old)) {
					replace.append(snew);
					i += old.length() - 1;
				} else {
					replace.append(cadena.substring(i, i + 1));
				}
			} else
				replace.append(cadena.substring(i, i + 1));
		}
		return replace.toString();
	}

	/**
	 * The Constructor.
	 */
	private GeneratorPathUtil() {

	}

}
