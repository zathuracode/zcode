package org.zcode.generator.robot.skyjet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Properties;

import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zcode.eclipse.plugin.generator.utilities.EclipseGeneratorUtil;
import org.zcode.generator.exceptions.GeneratorNotFoundException;
import org.zcode.generator.factory.ZathuraGeneratorFactory;
import org.zcode.generator.model.IZathuraGenerator;
import org.zcode.generator.utilities.GeneratorUtil;
import org.zcode.metadata.model.MetaDataModel;
import org.zcode.metadata.reader.IMetaDataReader;
import org.zcode.metadata.reader.MetaDataReaderFactory;
import org.zcode.reverse.engine.IZathuraReverseEngineering;
import org.zcode.reverse.engine.ZathuraReverseEngineering;
import org.zcode.reverse.utilities.ZathuraReverseEngineeringUtil;


public class SkyJetTest {
	
	private final static Logger log=LoggerFactory.getLogger(SkyJetTest.class);
		
	
	public static final String ZCODE_FULL_PATH=			"/Users/dgomez/Workspaces/workspace-2022-zcode/zcode/";
	
	public static final String WORKSPACE_PATH=			"/Users/dgomez/Workspaces/workspace-2022-zcode";
	public static final String PROJECT_PATH=			"/Users/dgomez/Workspaces/workspace-2022-zcode/demo-zcode-2022";
	public static final String POM_PATH=				"/Users/dgomez/Workspaces/workspace-2022-zcode/demo-zcode-2022/pom.xml";
	public static final String JAVA_SOURCE_CODE_PATH=	"/Users/dgomez/Workspaces/workspace-2022-zcode/demo-zcode-2022/src/main/java/";
	
	public static final String DOMAIN_PACKAGE_NAME = "com.vobi.bank.domain";
	public static final String PROJECT_NAME = "demo-zcode-2022";
	

	public static File pomFile =new File(POM_PATH);
		

	public static void main(String[] args) {
		try {		
			
			MetaDataModel metaDataModel = null;
			 
			ZathuraReverseEngineeringUtil.setFullPath(ZCODE_FULL_PATH);
			GeneratorUtil.setFullPath(ZCODE_FULL_PATH);
			
			//Del Proyecto
			EclipseGeneratorUtil.workspaceFolderPath=WORKSPACE_PATH;
			
			
			//Cargo los generadores
			ZathuraGeneratorFactory.loadZathuraGenerators();			
			IZathuraGenerator zathuraGenerator=ZathuraGeneratorFactory.createZathuraGenerator("SkyJet");			
			EclipseGeneratorUtil.metaDataReader = MetaDataReaderFactory.JPAEntityLoaderEngine;
			
			EclipseGeneratorUtil.fullPathProject=PROJECT_PATH;
			EclipseGeneratorUtil.javaClassFolderPath=JAVA_SOURCE_CODE_PATH;
			
			EclipseGeneratorUtil.javaEntityPackage=DOMAIN_PACKAGE_NAME;
			EclipseGeneratorUtil.companyDomainName=DOMAIN_PACKAGE_NAME;
			
			EclipseGeneratorUtil.projectName=PROJECT_NAME;
			EclipseGeneratorUtil.javaSourceFolderPath=JAVA_SOURCE_CODE_PATH;
			
			
			
			
			//Maven POM JDBC Connector
			EclipseGeneratorUtil.connectionGroupId="org.postgresql";
			EclipseGeneratorUtil.connectionArtifactId="postgresql";
			EclipseGeneratorUtil.connectionVersion="42.4.0";
			
			//EclipseGeneratorUtil.isMavenProject=true;
			
			//Para generacion de los Entity	
			EclipseGeneratorUtil.connectionDriverClass="org.postgresql.Driver";
			EclipseGeneratorUtil.connectionUrl="jdbc:postgresql://127.0.0.1:5432/bank";
			EclipseGeneratorUtil.connectionUsername="postgres";
			EclipseGeneratorUtil.connectionPassword="postgres";
			

			EclipseGeneratorUtil.schema="public";
			EclipseGeneratorUtil.catalogAndSchema="2";
			EclipseGeneratorUtil.catalog=null;
			EclipseGeneratorUtil.tablesList= Arrays.asList(
					"document_type"
					,"customer"
					,"account"
					,"registered_account"
					,"transaction"
					,"user_type"
					,"users"
					,"transaction_type");
	

			Properties connectionProperties = new Properties();
			
			
			connectionProperties.put("connectionDriverClass", EclipseGeneratorUtil.connectionDriverClass);
			connectionProperties.put("connectionUrl", EclipseGeneratorUtil.connectionUrl);
	
			connectionProperties.put("connectionUsername", EclipseGeneratorUtil.connectionUsername);
			connectionProperties.put("connectionPassword", EclipseGeneratorUtil.connectionPassword);
			connectionProperties.put("companyDomainName", EclipseGeneratorUtil.companyDomainName);
	
			connectionProperties.put("destinationDirectory", JAVA_SOURCE_CODE_PATH);
			
			connectionProperties.put("catalogAndSchema", EclipseGeneratorUtil.catalogAndSchema == null ? "" : EclipseGeneratorUtil.catalogAndSchema);
			connectionProperties.put("schema", EclipseGeneratorUtil.schema == null ? "" : EclipseGeneratorUtil.schema);
			connectionProperties.put("catalog", EclipseGeneratorUtil.catalog == null ? "" : EclipseGeneratorUtil.catalog);
	
			log.info("Delete folder in "+JAVA_SOURCE_CODE_PATH);
			// Borrar carpeta de temporales src/
			GeneratorUtil.deleteFiles(JAVA_SOURCE_CODE_PATH);
			log.info("Create folder in "+JAVA_SOURCE_CODE_PATH);
			// Crea carpeta de temporales
			GeneratorUtil.createFolder(JAVA_SOURCE_CODE_PATH);
	
			IZathuraReverseEngineering mappingTool = new ZathuraReverseEngineering();
			mappingTool.makePojosJPA_V1_0(connectionProperties, EclipseGeneratorUtil.tablesList);
			
			// Para que no corte los nombres de los paquetes
			Integer specificityLevel = 1;
			
			
			
			//carga
			IMetaDataReader entityLoader = null;
			entityLoader = MetaDataReaderFactory.createMetaDataReader(MetaDataReaderFactory.JPAEntityLoaderEngine);
			metaDataModel = entityLoader.loadMetaDataModel(JAVA_SOURCE_CODE_PATH, DOMAIN_PACKAGE_NAME);
			
			

			// Variables para el properties
			Properties properties = new Properties();
			properties.put("jpaPath", JAVA_SOURCE_CODE_PATH);
			properties.put("jpaPckgName", DOMAIN_PACKAGE_NAME);
			properties.put("specificityLevel", specificityLevel);
			
			properties.put("libFolderPath", "");
			properties.put("folderProjectPath", JAVA_SOURCE_CODE_PATH);
			properties.put("isMavenProject", true);
			properties.put("pomFile", pomFile);
			
			String MAIN_RESOURCES=	GeneratorUtil.slash+"src"+GeneratorUtil.slash+"main"+GeneratorUtil.slash+"resources"+GeneratorUtil.slash;
			String TEST_JAVA=		GeneratorUtil.slash+"src"+GeneratorUtil.slash+"test"+GeneratorUtil.slash+"java"+GeneratorUtil.slash;
			String TEST_RESOURCES=	GeneratorUtil.slash+"src"+GeneratorUtil.slash+"test"+GeneratorUtil.slash+"resources"+GeneratorUtil.slash;
			
			properties.put("mainResoruces", PROJECT_PATH+MAIN_RESOURCES);
			properties.put("testJava", 		PROJECT_PATH+TEST_JAVA);
			properties.put("testResoruces", PROJECT_PATH+TEST_RESOURCES);
			properties.put("fullPathProject",PROJECT_PATH);

			
			GeneratorUtil.generateMavenDirectoryStructure(PROJECT_PATH);
			zathuraGenerator.toGenerate(metaDataModel, PROJECT_NAME, JAVA_SOURCE_CODE_PATH, properties);
			
			System.exit(1);
				
		} catch (FileNotFoundException e) {
			
			log.error(e.getMessage());
		} catch (InstantiationException e) {
			
			log.error(e.getMessage());
		} catch (IllegalAccessException e) {
			
			log.error(e.getMessage());
		} catch (ClassNotFoundException e) {
			
			log.error(e.getMessage());
		} catch (XMLStreamException e) {
			
			log.error(e.getMessage());
		} catch (GeneratorNotFoundException e) {
			
			log.error(e.getMessage());
		} catch (Exception e) {
			
			log.error(e.getMessage());
		}
	}

}
