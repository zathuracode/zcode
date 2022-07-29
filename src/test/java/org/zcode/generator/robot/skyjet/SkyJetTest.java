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


public class SkyJetTest {
	
	private final static Logger log=LoggerFactory.getLogger(SkyJetTest.class);
	
	private static MetaDataModel metaDataModel = null;
	
	static String fullPathProject="/Users/dgomez/Workspaces/workspace-2022-zcode/demo-zcode-2022";
	
	//La ruta donde estan los .class de las clases con anotaciones JPA
	static String jpaPath = fullPathProject+"/src/main/java/";
	
	
	static String jpaPckgName = "com.vobi.bank.domain";
	static String projectName = "demo-banco-jender-web";
	static String folderProjectPath = fullPathProject+"/src/main/java/";
	
	
	static File pomFile =new File(fullPathProject+"/pom.xml");
	
	public static void main(String[] args) {
		try {
			
			
			
			GeneratorUtil.setFullPath("/Users/dgomez/Workspaces/workspace-2022-zcode/zcode/");
			
			
			//Cargo los generadores
			ZathuraGeneratorFactory.loadZathuraGenerators();			
			IZathuraGenerator zathuraGenerator=ZathuraGeneratorFactory.createZathuraGenerator("SkyJet");
			EclipseGeneratorUtil.javaVersion="1.8";
			EclipseGeneratorUtil.metaDataReader = MetaDataReaderFactory.JPAEntityLoaderEngine;
			EclipseGeneratorUtil.fullPathProject=fullPathProject;
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
			
			EclipseGeneratorUtil.workspaceFolderPath="/Users/dgomez/Workspaces/workspace-2022-zcode";
			EclipseGeneratorUtil.destinationDirectory="/demo-zcode-2022/src/main/java";
			//EclipseGeneratorUtil.connectionDriverJarPath="/Users/dgomez/Software/java/jdbc/postgresql-9.4.1211.jre6.jar";
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
			int specificityLevel = 1;
			
			if (metaDataModel == null) {
				IMetaDataReader entityLoader = null;
				entityLoader = MetaDataReaderFactory.createMetaDataReader(MetaDataReaderFactory.JPAEntityLoaderEngine);
				metaDataModel = entityLoader.loadMetaDataModel(jpaPath, jpaPckgName);
			}
			

			// Variables para el properties
			Properties properties = new Properties();
			properties.put("jpaPath", jpaPath);
			properties.put("jpaPckgName", jpaPckgName);
			properties.put("specificityLevel", new Integer(specificityLevel));
			
			properties.put("libFolderPath", "");
			properties.put("folderProjectPath", folderProjectPath);
			properties.put("isMavenProject", true);
			properties.put("pomFile", pomFile);
			
			String MAIN_RESOURCES=	GeneratorUtil.slash+"src"+GeneratorUtil.slash+"main"+GeneratorUtil.slash+"resources"+GeneratorUtil.slash;
			String TEST_JAVA=		GeneratorUtil.slash+"src"+GeneratorUtil.slash+"test"+GeneratorUtil.slash+"java"+GeneratorUtil.slash;
			String TEST_RESOURCES=	GeneratorUtil.slash+"src"+GeneratorUtil.slash+"test"+GeneratorUtil.slash+"resources"+GeneratorUtil.slash;
			
			properties.put("mainResoruces", fullPathProject+MAIN_RESOURCES);
			properties.put("testJava", 		fullPathProject+TEST_JAVA);
			properties.put("testResoruces", fullPathProject+TEST_RESOURCES);

			
			//EclipseGeneratorUtil.generate();
			
			GeneratorUtil.generateMavenDirectoryStructure(fullPathProject);
			zathuraGenerator.toGenerate(metaDataModel, projectName, folderProjectPath, properties);
				
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
