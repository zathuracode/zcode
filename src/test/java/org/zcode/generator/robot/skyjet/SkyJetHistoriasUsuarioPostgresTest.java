package org.zcode.generator.robot.skyjet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zcode.generator.exceptions.GeneratorNotFoundException;
import org.zcode.generator.factory.ZathuraGeneratorFactory;
import org.zcode.generator.model.IZathuraGenerator;
import org.zcode.generator.utilities.GeneratorPathUtil;
import org.zcode.generator.utilities.GeneratorUtil;
import org.zcode.metadata.model.MetaDataModel;
import org.zcode.metadata.reader.IMetaDataReader;
import org.zcode.metadata.reader.MetaDataReaderFactory;
import org.zcode.reverse.engine.IZathuraReverseEngineering;
import org.zcode.reverse.engine.ZathuraReverseEngineering;
import org.zcode.reverse.utilities.ZathuraReverseEngineeringUtil;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class SkyJetHistoriasUsuarioPostgresTest {
	
	private final static Logger log=LoggerFactory.getLogger(SkyJetHistoriasUsuarioPostgresTest.class);
		
	
	public static final String ZCODE_FULL_PATH=			"/Users/dgomez/Workspaces/workspace-2022-zcode/zcode/";
	
	public static final String WORKSPACE_PATH=			"/Users/dgomez/Workspaces/workspace-2022-zcode";
	public static final String PROJECT_PATH=			"/Users/dgomez/Workspaces/workspace-2022-zcode/historias-usuario";
	public static final String POM_PATH=				"/Users/dgomez/Workspaces/workspace-2022-zcode/historias-usuario/pom.xml";
	public static final String JAVA_SOURCE_CODE_PATH=	"/Users/dgomez/Workspaces/workspace-2022-zcode/historias-usuario/src/main/java/";
	
	
	
	public static final String DOMAIN_PACKAGE_NAME = "com.vortexbird.hu.domain";
	public static final String PROJECT_NAME = "historias-usuario";
	
	
	//DATABASE CONNECTION
	public static final String DRIVER_CLASS	=	"org.postgresql.Driver";
	public static final String URL=				"jdbc:postgresql://database-vortexbird.c8dq5oiucrly.us-east-1.rds.amazonaws.com:5432/historias_usuario";
	public static final String USER=			"historias_usuario_admin";
	public static final String PASSWORD=		"123456";
	public static final String SCHEMA=			"public";
	public static final String CATALOG=			null;
	public static final List<String> TABLE_LIST=Arrays.asList(
			"hu_plantilla"
			,"hu_empresa"
			,"hu_tipo_plantilla"
			,"hu_proyecto"
			,"hu_pila_producto"
			,"hu_historia_usuario"
			,"hu_epica"
			,"hu_responsable");
	
	//MAVEN DRIVER
	public static final String GROUP_ID="org.postgresql";
	public static final String ARTIFACT_ID="postgresql";
	public static final String VERSION="42.4.0";
	
	
	

	public static void main(String[] args) {
		try {		
			
			MetaDataModel metaDataModel = null;
			 
			ZathuraReverseEngineeringUtil.setFullPath(ZCODE_FULL_PATH);
			GeneratorUtil.setFullPath(ZCODE_FULL_PATH);
			
			GeneratorPathUtil.fullPathProject=		PROJECT_PATH;
			
			GeneratorPathUtil.javaClassFolderPath=	JAVA_SOURCE_CODE_PATH;
			GeneratorPathUtil.javaSourceFolderPath=	JAVA_SOURCE_CODE_PATH;
			
			GeneratorPathUtil.javaEntityPackage=	DOMAIN_PACKAGE_NAME;
			
			//MAVEN
			GeneratorPathUtil.projectName=			PROJECT_NAME;
			GeneratorPathUtil.companyDomainName=	DOMAIN_PACKAGE_NAME;
			
			
			
			
			
			
			//Maven POM JDBC Connector
			GeneratorPathUtil.connectionGroupId=GROUP_ID;
			GeneratorPathUtil.connectionArtifactId=ARTIFACT_ID;
			GeneratorPathUtil.connectionVersion=VERSION;
			
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
			
			//carga
			IMetaDataReader entityLoader = null;
			entityLoader = MetaDataReaderFactory.createMetaDataReader(MetaDataReaderFactory.JPAEntityLoaderEngine);
			metaDataModel = entityLoader.loadMetaDataModel(JAVA_SOURCE_CODE_PATH, DOMAIN_PACKAGE_NAME);

			// Variables para el properties de generacion de codigo
			Properties properties = new Properties();
			properties.put("jpaPath", JAVA_SOURCE_CODE_PATH);
			properties.put("jpaPckgName", DOMAIN_PACKAGE_NAME);
			
			properties.put("libFolderPath", "");
			properties.put("folderProjectPath", JAVA_SOURCE_CODE_PATH);
			properties.put("isMavenProject", true);
			
			
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
		}finally {
			System.exit(1);
		}
	}

}
