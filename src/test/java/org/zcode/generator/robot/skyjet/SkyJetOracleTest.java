package org.zcode.generator.robot.skyjet;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Properties;

import javax.xml.stream.XMLStreamException;

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


public class SkyJetOracleTest {
	
	private final static Logger log=LoggerFactory.getLogger(SkyJetOracleTest.class);
		
	
	public static final String ZCODE_FULL_PATH=			"/Users/dgomez/Workspaces/workspace-2022-zcode/zcode/";
	
	public static final String WORKSPACE_PATH=			"/Users/dgomez/Workspaces/workspace-2022-zcode";
	public static final String PROJECT_PATH=			"/Users/dgomez/Workspaces/workspace-2022-zcode/cuenta-coomeva";
	public static final String POM_PATH=				"/Users/dgomez/Workspaces/workspace-2022-zcode/cuenta-coomeva/pom.xml";
	public static final String JAVA_SOURCE_CODE_PATH=	"/Users/dgomez/Workspaces/workspace-2022-zcode/cuenta-coomeva/src/main/java/";
	
	
	public static final String DOMAIN_PACKAGE_NAME = "co.com.coomeva.cuentacoomevabackrest.domain";
	public static final String PROJECT_NAME = "cuenta-coomeva";
	
	
	//DATABASE CONNECTION
	public static final String DRIVER_CLASS	=	"oracle.jdbc.driver.OracleDriver";
	public static final String URL=				"jdbc:oracle:thin:@//bdwfdes.intracoomeva.com.co:1534/WFDES";
	public static final String USER=			"passport";
	public static final String PASSWORD=		"pasa*34mtx";
	
	//MAVEN DRIVER
	public static final String GROUP_ID="com.oracle.database.jdbc";
	public static final String ARTIFACT_ID="ojdbc11";
	public static final String VERSION="21.6.0.0.1";

	public static void main(String[] args) {
		try {		
			
			MetaDataModel metaDataModel = null;
			 
			ZathuraReverseEngineeringUtil.setFullPath(ZCODE_FULL_PATH);
			GeneratorUtil.setFullPath(ZCODE_FULL_PATH);
			
			GeneratorPathUtil.fullPathProject=		PROJECT_PATH;
			
			GeneratorPathUtil.javaClassFolderPath=	JAVA_SOURCE_CODE_PATH;
			GeneratorPathUtil.javaSourceFolderPath=	JAVA_SOURCE_CODE_PATH;
			
			GeneratorPathUtil.javaEntityPackage=	DOMAIN_PACKAGE_NAME;
			GeneratorPathUtil.companyDomainName=	DOMAIN_PACKAGE_NAME;
			
			GeneratorPathUtil.projectName=PROJECT_NAME;
			
			
			//Del Proyecto
			GeneratorPathUtil.workspaceFolderPath=WORKSPACE_PATH;
			
			
			//Maven POM JDBC Connector
			GeneratorPathUtil.connectionGroupId=GROUP_ID;
			GeneratorPathUtil.connectionArtifactId=ARTIFACT_ID;
			GeneratorPathUtil.connectionVersion=VERSION;
			
			//Para generacion de los Entity	
			GeneratorPathUtil.connectionDriverClass=DRIVER_CLASS;
			GeneratorPathUtil.connectionUrl=URL;
			GeneratorPathUtil.connectionUsername=USER;
			GeneratorPathUtil.connectionPassword=PASSWORD;
			

			GeneratorPathUtil.schema="PASSPORT";
			//GeneratorPathUtil.catalogAndSchema="2";
			GeneratorPathUtil.catalog=null;
			GeneratorPathUtil.tablesList= Arrays.asList(
					"BANK_EMPLOYEE"
					,"DEPARTMENT"
					,"EMPLOYEE"
					,"FRW_APLICACION"
					,"FRW_CLIENTE"
					,"FRW_CONTEXT"
					,"FRW_TOKEN"
					,"FRW_TRAZABILIDAD"
					,"PAS_APLICACION"
					,"PAS_APLICACION_EMPRESA"
					,"PAS_CAMBIO_ESTADO"
					,"PAS_EMPRESA"
					,"PAS_LOG"
					,"PAS_P_GRUPO_PARAMETRO"
					,"PAS_P_PARAMETRO"
					,"PAS_TENENCIA"
					,"PAS_TENENCIA_T"
					,"PAS_TIPO_LOG"
					,"PAS_TIPO_TRANSACCION"
					,"PAS_USUARIO"
					,"PAS_USUARIO_P"
					,"PAS_USUARIO_TEMP"
					,"PRO_ACCION"
					,"PRO_ADMIN"
					,"PRO_APLICACION"
					,"PRO_APLICACION_EMPRESA"
					,"PRO_LOG"
					,"PRO_LOG_TEMP"
					,"PRO_PARAMETRO"
					,"PRO_ROL"
					,"PRO_ROL_ACCION"
					,"PRO_ROL_USUARIO"
					,"PRO_SECCION"
					,"PRO_TIPO_PARAMETRO"
					,"PRO_USUARIO"
					,"SOCIAL_SECURITY"
					);
	

			Properties connectionProperties = new Properties();
			
			
			connectionProperties.put("connectionDriverClass", GeneratorPathUtil.connectionDriverClass);
			connectionProperties.put("connectionUrl", GeneratorPathUtil.connectionUrl);
	
			connectionProperties.put("connectionUsername", GeneratorPathUtil.connectionUsername);
			connectionProperties.put("connectionPassword", GeneratorPathUtil.connectionPassword);
			connectionProperties.put("companyDomainName", GeneratorPathUtil.companyDomainName);
	
			connectionProperties.put("destinationDirectory", JAVA_SOURCE_CODE_PATH);
			
			//connectionProperties.put("catalogAndSchema", GeneratorPathUtil.catalogAndSchema == null ? "" : GeneratorPathUtil.catalogAndSchema);
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
