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
import org.zcode.reverse.utilities.ZathuraReverseEngineeringUtil;


public class SkyJetTest {
	
	
	public static final String ZCODE_FULL_PATH="/Users/dgomez/Workspaces/workspace-2022-zcode/zcode/";
	public static final String PROJECT_FULL_PATH="/Users/dgomez/Workspaces/workspace-2022-zcode/demo-zcode-2022";
	
	//La ruta donde estan los .class de las clases con anotaciones JPA
	public static String jpaPath = PROJECT_FULL_PATH+"/src/main/java/";
	public static String folderProjectPath = PROJECT_FULL_PATH+"/src/main/java/";
	public static File pomFile =new File(PROJECT_FULL_PATH+"/pom.xml");
	
	
	private final static Logger log=LoggerFactory.getLogger(SkyJetTest.class);
	
	private static MetaDataModel metaDataModel = null;
	
	static String jpaPckgName = "com.vobi.bank.domain";
	static String projectName = "demo-banco-jender-web";

	
	public static void main(String[] args) {
		try {
			
			
			
			ZathuraReverseEngineeringUtil.setFullPath(ZCODE_FULL_PATH);
			GeneratorUtil.setFullPath(ZCODE_FULL_PATH);
			
			EclipseGeneratorUtil.workspaceFolderPath="/Users/dgomez/Workspaces/workspace-2022-zcode";
			EclipseGeneratorUtil.destinationDirectory="/demo-zcode-2022/src/main/java";
			
			
			
			
			//Cargo los generadores
			ZathuraGeneratorFactory.loadZathuraGenerators();			
			IZathuraGenerator zathuraGenerator=ZathuraGeneratorFactory.createZathuraGenerator("SkyJet");
			EclipseGeneratorUtil.javaVersion="1.8";
			EclipseGeneratorUtil.metaDataReader = MetaDataReaderFactory.JPAEntityLoaderEngine;
			EclipseGeneratorUtil.fullPathProject=PROJECT_FULL_PATH;
			EclipseGeneratorUtil.javaClassFolderPath=jpaPath;
			EclipseGeneratorUtil.javaEntityPackage=jpaPckgName;
			EclipseGeneratorUtil.projectName=projectName;
			EclipseGeneratorUtil.javaSourceFolderPath=folderProjectPath;
			
			
			EclipseGeneratorUtil.companyDomainName=jpaPckgName;
			
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
			EclipseGeneratorUtil.companyDomainName=jpaPckgName;

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
	

			// Genera los entity originales
			EclipseGeneratorUtil.generateJPAReverseEngineering();

			// Para que no corte los nombres de los paquetes
			Integer specificityLevel = 1;
			
			if (metaDataModel == null) {
				IMetaDataReader entityLoader = null;
				entityLoader = MetaDataReaderFactory.createMetaDataReader(MetaDataReaderFactory.JPAEntityLoaderEngine);
				metaDataModel = entityLoader.loadMetaDataModel(jpaPath, jpaPckgName);
			}
			

			// Variables para el properties
			Properties properties = new Properties();
			properties.put("jpaPath", jpaPath);
			properties.put("jpaPckgName", jpaPckgName);
			properties.put("specificityLevel", specificityLevel);
			
			properties.put("libFolderPath", "");
			properties.put("folderProjectPath", folderProjectPath);
			properties.put("isMavenProject", true);
			properties.put("pomFile", pomFile);
			
			String MAIN_RESOURCES=	GeneratorUtil.slash+"src"+GeneratorUtil.slash+"main"+GeneratorUtil.slash+"resources"+GeneratorUtil.slash;
			String TEST_JAVA=		GeneratorUtil.slash+"src"+GeneratorUtil.slash+"test"+GeneratorUtil.slash+"java"+GeneratorUtil.slash;
			String TEST_RESOURCES=	GeneratorUtil.slash+"src"+GeneratorUtil.slash+"test"+GeneratorUtil.slash+"resources"+GeneratorUtil.slash;
			
			properties.put("mainResoruces", PROJECT_FULL_PATH+MAIN_RESOURCES);
			properties.put("testJava", 		PROJECT_FULL_PATH+TEST_JAVA);
			properties.put("testResoruces", PROJECT_FULL_PATH+TEST_RESOURCES);
			properties.put("fullPathProject",PROJECT_FULL_PATH);


			
			//EclipseGeneratorUtil.generate();
			
			GeneratorUtil.generateMavenDirectoryStructure(PROJECT_FULL_PATH);
			zathuraGenerator.toGenerate(metaDataModel, projectName, folderProjectPath, properties);
			
			System.exit(1);
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (GeneratorNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
	}

}
