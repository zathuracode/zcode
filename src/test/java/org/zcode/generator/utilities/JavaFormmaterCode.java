package org.zcode.generator.utilities;

import static com.google.common.base.StandardSystemProperty.JAVA_CLASS_PATH;
import static com.google.common.base.StandardSystemProperty.JAVA_HOME;

import java.nio.file.Paths;

import com.google.common.collect.ImmutableList;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

public class JavaFormmaterCode {
	
	 private static final ImmutableList<String> ADD_EXPORTS =
		      ImmutableList.of(
		          "--add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
		          "--add-exports=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED",
		          "--add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
		          "--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
		          "--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
		          "--add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
		          "--add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED");
	
	public static void main(String[] args) {
		
		System.out.println(Paths.get(JAVA_HOME.value()));
		System.out.println(JAVA_CLASS_PATH.value());
		
		
		Formatter formatter = new Formatter();
	      try {
	        System.out.println(formatter.formatSource("public class Test{public static void main(String[] args) {String s;}}"));
	      } catch (FormatterException e) {
	        e.printStackTrace();
	    }
		
	}

}
