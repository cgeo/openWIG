package cz.matejcik.openwig.android;

import cz.matejcik.openwig.platform.FileHandle;
import java.io.*;

public class AndroidFileHandle implements FileHandle {

	private File file;

	public AndroidFileHandle (File file) {
		this.file = file;
	}

	public DataInputStream openDataInputStream () throws IOException {
		return new DataInputStream(new FileInputStream(file));
	}

	public DataOutputStream openDataOutputStream () throws IOException {
		return new DataOutputStream(new FileOutputStream(file));
	}

	public boolean exists () throws IOException {
		return file.exists();
	}

	public void create () throws IOException {
		file.createNewFile();
	}

	public void delete () throws IOException {
		file.delete();
	}

	public void truncate (long len) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "w");
		raf.setLength(len);
		raf.close();
	}

}
