package org.zcode.generator.utilities;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

public class JavaFormmaterCode {
	
	public static void main(String[] args) {
		Formatter formatter = new Formatter();
	      try {
	        System.out.println(formatter.formatSource("public class Test{public static void main(String[] args) {String s;}}"));
	      } catch (FormatterException e) {
	        e.printStackTrace();
	    }
		
	}

}
