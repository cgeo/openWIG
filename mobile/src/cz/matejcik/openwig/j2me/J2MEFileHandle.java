package cz.matejcik.openwig.j2me;

import cz.matejcik.openwig.platform.FileHandle;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.file.FileConnection;

public class J2MEFileHandle implements FileHandle {

	private FileConnection conn;

	public J2MEFileHandle (FileConnection conn) {
		this.conn = conn;
	}

	public DataInputStream openDataInputStream () throws IOException {
		return conn.openDataInputStream();
	}

	public DataOutputStream openDataOutputStream () throws IOException {
		return conn.openDataOutputStream();
	}

	public boolean exists () throws IOException {
		return conn.exists();
	}

	public void create () throws IOException {
		conn.create();
	}

	public void delete () throws IOException {
		conn.delete();
	}

	public void truncate (long len) throws IOException {
		conn.truncate(len);
	}
}
