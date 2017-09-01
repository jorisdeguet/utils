package org.deguet.gutils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class FileString {

	public final static String fromFile(final String path) throws IOException
	{
		FileInputStream fis = new FileInputStream(new File(path));
		try {
			FileChannel fc = fis.getChannel();
			MappedByteBuffer bytebuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			CharBuffer       charbuffer = Charset.defaultCharset().decode(bytebuffer);
			return charbuffer.toString();
		}
		finally {
			fis.close();
		}
	}

	/**
	 * Write a string object to a text file.
	 * @param f the target file
	 * @param content the content to write in this file.
	 * @return
	 * @throws IOException 
	 */
	public final static boolean  toFile(final File f,final String content) throws IOException{
		FileWriter fw = new FileWriter(f);
		fw.write(content);
		fw.close();
		return true;
	}
}
