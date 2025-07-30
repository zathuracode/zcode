package org.zcode.generator.utilities;

import java.io.File;

import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.googlejavaformat.java.Formatter;

import lombok.extern.slf4j.Slf4j;

/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 2.0
 */
@Slf4j
public class GoogleCodeFormatter {

	

	/**
	 * The Constructor.
	 */
	private GoogleCodeFormatter() {}
	
	public static void formatJavaCodeFile(String pathFiles) {
		
		log.info("GoogleCodeFormatter Java in file:"+pathFiles);
		
		try {
			
			if (pathFiles.endsWith(".java")) {
				File file = new File(pathFiles);
			    CharSource source = Files.asCharSource(file, Charsets.UTF_8);
			    CharSink output = Files.asCharSink(file, Charsets.UTF_8);
			    
			    //JavaFormatterOptions options = JavaFormatterOptions.builder().style(Style.GOOGLE).build();
			    
			    new Formatter().formatSource(source, output);			
			}
			
		} catch (Exception e) {
			log.info("Error in formatJavaCodeFile:"	+ e.toString());
		}
		
	}
	

}
