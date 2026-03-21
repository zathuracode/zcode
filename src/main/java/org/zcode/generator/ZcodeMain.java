package org.zcode.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.zcode.generator.exceptions.GeneratorNotFoundException;
import org.zcode.generator.factory.ZathuraGeneratorFactory;
import org.zcode.generator.model.IZathuraGenerator;
import org.zcode.generator.utilities.GeneratorPathUtil;
import org.zcode.generator.utilities.GeneratorUtil;
import org.zcode.metadata.model.MetaDataModel;
import org.zcode.metadata.reader.IMetaDataReader;
import org.zcode.metadata.reader.MetaDataReaderFactory;
import org.zcode.metadata.utilities.MetaDataUtil;
import org.zcode.reverse.engine.IZathuraReverseEngineering;
import org.zcode.reverse.engine.ZathuraReverseEngineering;
import org.zcode.reverse.utilities.ZathuraReverseEngineeringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZcodeMain {
	
	public static  String PROJECT_PATH = null;
	
	public static String DOMAIN_PACKAGE_NAME = null;
	
	//MAVEN PROPERTIES
	public static String GROUP_ID = null;
	public static String PROJECT_NAME = null;

	// DATABASE CONNECTION
	public static String DRIVER_CLASS = null;
	public static String URL = null;
	public static String USER = null;
	public static String PASSWORD = null;
	public static String SCHEMA = null;
	public static String CATALOG = null;
	public static List<String> TABLE_LIST = null;
	
	private static Configuration configuration =null;
	private static int exitCode = 0;

	
	public static void main(String[] args) {		
		
		printBanner();
		jdkInfo();
		log.info("CurrentWorkingDirectory:{}",GeneratorUtil.getCurrentWorkingDirectory());
		loadProperties();
		
		try {		
			
			MetaDataModel metaDataModel = null;
			 
			ZathuraReverseEngineeringUtil.setFullPath(GeneratorUtil.getCurrentWorkingDirectory());
			
			//Load Maven Properties Database Coneccion
			ZathuraReverseEngineeringUtil.loadDataBaseMaven(DRIVER_CLASS);
			
			
			
			String JAVA_SOURCE_CODE_PATH=	PROJECT_PATH+File.separator+"src/main/java/";
			
			
			GeneratorUtil.createFolder(PROJECT_PATH);
			GeneratorUtil.createFolder(JAVA_SOURCE_CODE_PATH);
			
			
			GeneratorPathUtil.fullPathProject=		PROJECT_PATH;
			
			GeneratorPathUtil.javaClassFolderPath=	JAVA_SOURCE_CODE_PATH;
			GeneratorPathUtil.javaSourceFolderPath=	JAVA_SOURCE_CODE_PATH;			
			GeneratorPathUtil.javaEntityPackage=	DOMAIN_PACKAGE_NAME;
			
			//MAVEN
			GeneratorPathUtil.projectName=			PROJECT_NAME;
			GeneratorPathUtil.groupIdMavenPoject=	GROUP_ID;
			
			//Domain 
			GeneratorPathUtil.companyDomainName=	DOMAIN_PACKAGE_NAME;
			
			//Para generacion de los Entity	
			GeneratorPathUtil.connectionDriverClass=DRIVER_CLASS;
			GeneratorPathUtil.connectionUrl=URL;
			GeneratorPathUtil.connectionUsername=USER;
			GeneratorPathUtil.connectionPassword=PASSWORD;
			

			GeneratorPathUtil.schema=SCHEMA;
			GeneratorPathUtil.catalog=CATALOG;
			GeneratorPathUtil.tablesList= TABLE_LIST;
	

			Properties connectionProperties = new Properties();
			
			
			connectionProperties.put("connectionDriverClass", 	GeneratorPathUtil.connectionDriverClass);
			connectionProperties.put("connectionUrl", 			GeneratorPathUtil.connectionUrl);
	
			connectionProperties.put("connectionUsername", 		GeneratorPathUtil.connectionUsername);
			connectionProperties.put("connectionPassword",		GeneratorPathUtil.connectionPassword);
			connectionProperties.put("companyDomainName", 		GeneratorPathUtil.companyDomainName);
			connectionProperties.put("javaEntityPackage", 		GeneratorPathUtil.javaEntityPackage);
			
			connectionProperties.put("destinationDirectory", JAVA_SOURCE_CODE_PATH);
			
			
			connectionProperties.put("schema", GeneratorPathUtil.schema == null ? "" : GeneratorPathUtil.schema);
			connectionProperties.put("catalog", GeneratorPathUtil.catalog == null ? "" : GeneratorPathUtil.catalog);
	
			log.info("Delete folder in "+JAVA_SOURCE_CODE_PATH);
			// Borrar carpeta de temporales src/
			GeneratorUtil.deleteFiles(JAVA_SOURCE_CODE_PATH);
			log.info("Create folder in "+JAVA_SOURCE_CODE_PATH);
			// Crea carpeta de temporales
			GeneratorUtil.createFolder(JAVA_SOURCE_CODE_PATH);
	
			IZathuraReverseEngineering mappingTool = new ZathuraReverseEngineering();
			mappingTool.makePojosJPA_V1_0(connectionProperties, GeneratorPathUtil.tablesList);
			
			//Carga el La metadata de los pojos con JPA
			IMetaDataReader entityLoader = null;
			entityLoader = MetaDataReaderFactory.createMetaDataReader(MetaDataReaderFactory.JPAEntityLoaderEngine);
			metaDataModel = entityLoader.loadMetaDataModel(JAVA_SOURCE_CODE_PATH, DOMAIN_PACKAGE_NAME);
			
			//Delete .class
			MetaDataUtil.deleteClass(JAVA_SOURCE_CODE_PATH, DOMAIN_PACKAGE_NAME);

			// Variables para el properties de generacion de codigo
			Properties properties = new Properties();
			properties.put("jpaPath", JAVA_SOURCE_CODE_PATH);
			properties.put("jpaPckgName", DOMAIN_PACKAGE_NAME);
			
			properties.put("libFolderPath", "");
			properties.put("folderProjectPath", JAVA_SOURCE_CODE_PATH);
			properties.put("isMavenProject", true);
			
			//Se crean las cadenas de los path
			String MAIN_RESOURCES=	GeneratorUtil.slash+"src"+GeneratorUtil.slash+"main"+GeneratorUtil.slash+"resources"+GeneratorUtil.slash;
			String TEST_JAVA=		GeneratorUtil.slash+"src"+GeneratorUtil.slash+"test"+GeneratorUtil.slash+"java"+GeneratorUtil.slash;
			String TEST_RESOURCES=	GeneratorUtil.slash+"src"+GeneratorUtil.slash+"test"+GeneratorUtil.slash+"resources"+GeneratorUtil.slash;
			
			properties.put("mainResoruces", PROJECT_PATH+MAIN_RESOURCES);
			properties.put("testJava", 		PROJECT_PATH+TEST_JAVA);
			properties.put("testResoruces", PROJECT_PATH+TEST_RESOURCES);
			properties.put("fullPathProject",PROJECT_PATH);
			
			GeneratorUtil.generateMavenDirectoryStructure(PROJECT_PATH);
			
			//Cargo los generadores
			ZathuraGeneratorFactory.loadZathuraGenerators();			
			IZathuraGenerator zathuraGenerator=ZathuraGeneratorFactory.createZathuraGenerator("SkyJet");		
			zathuraGenerator.toGenerate(metaDataModel, PROJECT_NAME, JAVA_SOURCE_CODE_PATH, properties);
			
			
				
		} catch (FileNotFoundException e) {			
			exitCode = 1;
			log.error(e.getMessage());
		} catch (InstantiationException e) {			
			exitCode = 1;
			log.error(e.getMessage());
		} catch (IllegalAccessException e) {			
			exitCode = 1;
			log.error(e.getMessage());
		} catch (ClassNotFoundException e) {			
			exitCode = 1;
			log.error(e.getMessage());
		} catch (XMLStreamException e) {			
			exitCode = 1;
			log.error(e.getMessage());
		} catch (GeneratorNotFoundException e) {			
			exitCode = 1;
			log.error(e.getMessage());
		} catch (Exception e) {			
			exitCode = 1;
			log.error(e.getMessage());
		}finally {
			System.exit(exitCode);
		}
		
		
	
	}
	
	private static void loadProperties() {
		log.info("Reading Configuration Properties");
		
			configuration = loadConfig();	
			
			PROJECT_PATH = 			configuration.getString("PROJECT_PATH");
			
			//MAVEN
			GROUP_ID = 				configuration.getString("GROUP_ID");
			PROJECT_NAME = 			configuration.getString("PROJECT_NAME");
			
			//DOMAIN
			DOMAIN_PACKAGE_NAME = 	configuration.getString("DOMAIN_PACKAGE_NAME");
	
			//DATABASE CONNECTION
			DRIVER_CLASS = 			configuration.getString("DRIVER_CLASS");
			URL = 					configuration.getString("URL");
			USER = 					configuration.getString("USER");
			PASSWORD = 				configuration.getString("PASSWORD");
			SCHEMA = 				configuration.getString("SCHEMA");
			CATALOG = 				configuration.getString("CATALOG");
			
			TABLE_LIST =			Arrays.asList(configuration.getString("TABLE_LIST").split(",")).stream().map(String::trim).collect(Collectors.toList());
			
			printProperties();
	}

	/**
	 * Print the configuration file
	 */
	private static void printProperties() {
		log.info("#------------------------------------------#");
			Iterator<String> keys=configuration.getKeys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				log.info("{}:{}",key,configuration.getString(key));
			}
		log.info("#------------------------------------------#");
	}
	
	/**
	 * Print the Welcome Banner
	 */
	private static void printBanner() {
		try {
			Files.readAllLines(Paths.get(GeneratorUtil.getCurrentWorkingDirectory()+File.separator+"config"+File.separator+"banner.txt")).forEach(line->System.out.println(line));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Configuration loadConfig() {
		Configurations configurations = new Configurations();
		try {
			Configuration configuration = configurations.properties(new File("zcode-gen.properties"));
			return configuration;
		} catch (ConfigurationException cex) {
			exitCode = 1;
			log.error(cex.getMessage());
		}
		return null;
	}
	
	private static void jdkInfo() {
		log.info("#----------------------JDK INFO--------------------#");
		 // Versión del runtime (Java 9+)
	    var v = Runtime.version();
	    log.info("Java version: " + v);                // ej. 21.0.3+7
	    log.info("Feature: " + v.feature());           // ej. 21
	    log.info("Interim: " + v.interim());
	    log.info("Update: " + v.update());
	    log.info("Build: " + v.build().orElse(null));

	    // Properties típicas
	    log.info("java.vendor: " + System.getProperty("java.vendor"));
	    log.info("java.vm.name: " + System.getProperty("java.vm.name"));

	    // Ruta del runtime (JAVA_HOME o la imagen del JDK/JRE en uso)
	    String javaHome = System.getProperty("java.home");       // ej. .../jdk-21.0.3
	    log.info("java.home: " + javaHome);

	    // Ruta del ejecutable java que lanzó el proceso
	    String javaCmd = java.lang.ProcessHandle.current()
	        .info()
	        .command()
	        .orElse("desconocido");
	    log.info("java executable: " + javaCmd);

	    // ¿Es JDK completo (tiene compilador) o solo JRE?
	    boolean esJdk = javax.tools.ToolProvider.getSystemJavaCompiler() != null;
	    log.info("is complete JDK?: " + esJdk);

	    // (Opcional) “Raíz” del JDK cuando java.home apunta a .../jre (antiguo Java 8)
	    Path raizAproximada = Paths.get(javaHome).getFileName().toString().equalsIgnoreCase("jre")
	        ? Paths.get(javaHome).getParent()
	        : Paths.get(javaHome);
	    log.info("Possible JDK root: " + raizAproximada);
		log.info("#----------------------JDK INFO--------------------#");

	}
	

}
