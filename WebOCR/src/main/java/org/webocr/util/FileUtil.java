package org.webocr.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileUtil {

    /**
     * Read a file's content into a text buffer.
     * 
     * @param file File to be read.
     * @return
     */
    public static String readFileAsString(File file, boolean emptyLines) {
	StringBuilder stringBuilder = new StringBuilder();

	try (FileReader in = new FileReader(file)) {
	    String line;

	    try (BufferedReader reader = new BufferedReader(in)) {
		while ((line = reader.readLine()) != null) {
		    if (!emptyLines) {
			if (!line.equals("")) {
			    stringBuilder.append(line + "\n");
			}
		    } else {
			stringBuilder.append(line + "\n");
		    }
		}
	    }
	} catch (Exception e) {
	    System.err.println("Cannot read file as string.\n" + e.getLocalizedMessage());
	}

	return stringBuilder.toString();
    }
}
