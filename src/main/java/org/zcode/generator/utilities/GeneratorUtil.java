package org.zcode.generator.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import lombok.extern.slf4j.Slf4j;

/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
@Slf4j
public class GeneratorUtil {

	private final static String generatorTemplatesPath = "templates";
	private final static String generatorExtPath = "ext";

	/** The slash. */
	public static String slash = System.getProperty("file.separator");

	public static String pomFile = "pom.xml";

	/** The File name. */
	public static String FileName = "zathura-generator-factory-config.xml";

	/** The full path. */
	private static String fullPath = "";

	/** The xml config factory path. */
	private static String xmlConfigFactoryPath = "config" + slash + FileName;

	/** The xml config. */
	private static String xmlConfig = "config" + slash;
	
	
	public static String getCurrentWorkingDirectory() {
		String currentWorkingDirectory=Paths.get("").toAbsolutePath().toString()+File.separatorChar;
		GeneratorUtil.setFullPath(currentWorkingDirectory);
		return currentWorkingDirectory;
	}

	/**
	 * Crea la estructura de caroetas del proyecto maven si no existe
	 */
	public static void generateMavenDirectoryStructure(String projectPath) {
		
		String MAIN_RESOURCES = GeneratorUtil.slash + "src" + GeneratorUtil.slash + "main" + GeneratorUtil.slash + "resources" + GeneratorUtil.slash;
		String MAIN_RESOURCES_ORM_XML = GeneratorUtil.slash + "src" + GeneratorUtil.slash + "main" + GeneratorUtil.slash + "resources" + GeneratorUtil.slash + "META-INF" + GeneratorUtil.slash;
		String TEST_JAVA = GeneratorUtil.slash + "src" + GeneratorUtil.slash + "test" + GeneratorUtil.slash + "java" + GeneratorUtil.slash;
		String TEST_RESOURCES = GeneratorUtil.slash + "src" + GeneratorUtil.slash + "test" + GeneratorUtil.slash + "resources" + GeneratorUtil.slash;

		log.info("Begin Validate Maven Directory Structure");
			projectPath = projectPath + GeneratorUtil.slash;
			createFolder(projectPath + MAIN_RESOURCES);
			createFolder(projectPath + MAIN_RESOURCES_ORM_XML);
			createFolder(projectPath + TEST_JAVA);
			createFolder(projectPath + TEST_RESOURCES);
		log.info("End Validate Maven Directory Structure");
	}

	/**
	 * Retorna el path de los templates de velocity
	 * 
	 * @return
	 */
	public static String getGeneratorTemplatesPath() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + generatorTemplatesPath;
		}
		return generatorTemplatesPath;
	}

	/**
	 * retorna el path de los extras del proyecto
	 * 
	 * @return
	 */
	public static String getGeneratorExtPath() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + generatorExtPath;
		}
		return generatorExtPath;
	}

	/**
	 * Gets the full path.
	 *
	 * @return the full path
	 */
	public static String getFullPath() {
		return fullPath;
	}

	/**
	 * Sets the full path.
	 *
	 * @param fullPath the full path
	 */
	public static void setFullPath(String fullPath) {
		// System.err.println(fullPath);
		if (fullPath != null && fullPath.startsWith("/")
				&& System.getProperty("os.name").toUpperCase().contains("WINDOWS") == true) {
			fullPath = fullPath.substring(1, fullPath.length());
		}

		if (fullPath != null) {
			if (slash.equals("/")) {
				fullPath = replaceAll(fullPath, "\\", slash);
				if (fullPath.endsWith("\\") == true) {
					fullPath = fullPath.substring(0, fullPath.length() - 1);
					fullPath = fullPath + slash;
				}

			} else if (slash.equals("\\")) {
				fullPath = replaceAll(fullPath, "/", slash);
				if (fullPath.endsWith("/") == true) {
					fullPath = fullPath.substring(0, fullPath.length() - 1);
					fullPath = fullPath + slash;
				}
			}
		}
		// System.err.println(fullPath);

		GeneratorUtil.fullPath = fullPath;
	}

	/**
	 * Gets the xml config factory path.
	 *
	 * @return the xml config factory path
	 */
	public static String getXmlConfigFactoryPath() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + xmlConfigFactoryPath;
		}
		return xmlConfigFactoryPath;
	}

	/**
	 * Gets the xml config.
	 *
	 * @return the xml config
	 */
	public static String getXmlConfig() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + xmlConfig + slash;
		}
		return xmlConfigFactoryPath;
	}

	/**
	 * Creates the folder.
	 *
	 * @param path the path
	 * @return the file
	 */
	public static File createFolder(String path) {
		File aFile = new File(path);
		aFile.mkdirs();
		return aFile;
	}

	/**
	 * Copy.
	 *
	 * @param source the source
	 * @param Target the Target
	 */
	public static void copy(String source, String Target) {
		FileInputStream fIn = null;
		FileOutputStream fOut = null;
		byte[] b;
		int l = 0;
		try {
			fIn = new FileInputStream(source);
			fOut = new FileOutputStream(Target);
			b = new byte[1024];
			while ((l = fIn.read(b)) > 0) {
				fOut.write(b, 0, l);
			}
			fOut.close();
			fIn.close();
		} catch (FileNotFoundException fnfe) {
			log.error(fnfe.toString());
		} catch (IOException ioe) {
			log.error(ioe.toString());
		}
	}

	/**
	 * Delete files.
	 *
	 * @param path the path
	 */
	public static void deleteFiles(String path) {
		File file = new File(path);
		deleteFiles(file);
	}

	/**
	 * Delete files.
	 *
	 * @param file the file
	 */
	public static void deleteFiles(File file) {
		if (file == null || !file.exists()) {
			return;
		}

		File[] listFiles = file.listFiles();
		if (listFiles == null) {
			file.delete();
			return;
		}

		for (File child : listFiles) {
			if (child.isDirectory()) {
				deleteFiles(child);
			}
			child.delete();
		}

		File[] remainingFiles = file.listFiles();
		if (remainingFiles != null && remainingFiles.length == 0) {
			file.delete();
		}
	}

	/**
	 * Delete file.
	 *
	 * @param path the path
	 */
	public static void deleteFile(String path) {
		File file = new File(path);
		deleteFile(file);
	}

	/**
	 * Delete file.
	 *
	 * @param file the file
	 */
	public static void deleteFile(File file) {
		file.delete();
	}

	/**
	 * public static List<String> copyFolder(String source,String target){
	 * List<String> filesLib =new ArrayList<String>(); try { File dir = new
	 * File(source); String[] fileNames = dir.list(); for (int i = 0; i <
	 * fileNames.length; i++) { String s=fileNames[i]; copy(source+s,target+s);
	 * filesLib.add(s); } return filesLib; } catch (Exception e) { // log4j
	 * System.out.println("Error copy all files of folder:"+e.toString()); } return
	 * null; }
	 *
	 * @param source the source
	 * @param target the target
	 */
	public static void copyFolder(String source, String target) {
		try {
			File dir = new File(source);
			File[] listFiles = dir.listFiles();
			File fileSource = null;
			for (int i = 0; i < listFiles.length; i++) {
				fileSource = listFiles[i];
				if (fileSource.isDirectory()) {
					log.debug(fileSource.getName());
					log.debug("Source:" + fileSource.getAbsolutePath());
					log.debug("Target:" + target + fileSource.getName());
					createFolder(target + fileSource.getName());
					copyFolder(fileSource.getAbsolutePath() + slash, target + fileSource.getName() + slash);
				} else {
					copy(fileSource.getAbsolutePath(), target + fileSource.getName());
					log.debug(fileSource.toString());
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * File fileAux = null; File listFiles[] = null; int iPos = -1;
	 * 
	 * listFiles = file.listFiles(); for(iPos = 0; iPos < listFiles.length; iPos++){
	 * fileAux = listFiles[iPos]; if(fileAux.isDirectory())
	 * deleteFiles(listFiles[iPos]); listFiles[iPos].delete(); }
	 * if(file.listFiles().length == 0) file.delete();
	 *
	 * @param path the path
	 * @param pck  the pck
	 * @return the string
	 */

	public static String createFolderOfPackage(String path, String pck) {
		try {
			path = path + replaceAll(pck, ".", File.separator);
			path = path + File.separator;
			File myDirectory = new File(path);
			myDirectory.mkdirs();
			return path;
		} catch (Exception e) {
			log.error("Fallo creacion de carpetas para los paquetes:" + e.toString());
		}
		return null;
	}

	/**
	 * Validate directory.
	 *
	 * @param packageName the package name
	 * @param location    the location
	 * @return true, if validate directory
	 * @throws IOException the IO exception
	 */
	public static boolean validateDirectory(String packageName, String location) throws IOException {

		if (location == null || location.equals("") == true)
			return false;

		File dirComm = new File(location);

		if (dirComm.exists()) {
			String dirsToCreate[] = packageName.split("_");

			for (int i = 0; i < dirsToCreate.length; i++) {
				location = location + slash + dirsToCreate[i];
				dirComm = new File(location);

				if (!dirComm.exists()) {
					dirComm.mkdir();
				}
			}
		}
		return true;
	}

	/**
	 * Replace all.
	 *
	 * @param cadena the cadena
	 * @param old    the old
	 * @param snew   the snew
	 * @return the string
	 * 
	 *         Esta utilidad permite remplazar en una cadena un valor por otro Ej:
	 *         String cadena= "com.mauricio.demogenerator"; String
	 *         salida=cadena.replaceAll(cadena,".","/"); salida =
	 *         com/mauricio/demogenerator
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
	
	

}
