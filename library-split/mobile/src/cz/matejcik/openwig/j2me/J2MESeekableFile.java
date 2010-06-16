package cz.matejcik.openwig.j2me;

import cz.matejcik.openwig.platform.SeekableFile;
import java.io.*;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import se.krka.kahlua.vm.LuaPrototype;

public class J2MESeekableFile implements SeekableFile {

	private FileConnection conn;
	private DataInputStream input;
	private long pos = 0;

	private void rewind () throws IOException {
		if (input != null) input.close();
		input = conn.openDataInputStream();
		pos = 0;
	}

	public J2MESeekableFile (String filename) throws IOException, SecurityException {
		this.conn = (FileConnection)Connector.open(filename, Connector.READ);
		rewind();
	}
	
	public J2MESeekableFile(FileConnection conn) throws IOException {
		this.conn = conn;
		rewind();
	}

	public static short flip (short s) {
		int a = s & 0xff;
		int b = (s >> 8) & 0xff;
		return (short)((a << 8) | b);
	}
	
	public short readShort() throws IOException {
		short s = flip(input.readShort());
		pos += 2;
		return s;
	}
	
	public int readInt() throws IOException {
		int i = LuaPrototype.rev(input.readInt());
		pos += 4;
		return i;
	}
	
	public String readString() throws IOException {
		StringBuffer sb = new StringBuffer();
		int b = input.read(); pos++;
		while (b > 0) {
			sb.append((char)b);
			b = input.read(); pos++;
		}
		return sb.toString();
	}
	
	public long readLong() throws IOException {
		long l = LuaPrototype.rev(input.readLong());
		pos += 8;
		return l;
	}
	
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}
	
	public void readFully (byte[] b) throws IOException {
		int r = 0;
		try {
			while (r < b.length) {
				int re = input.read(b, r, b.length - r);
				if (re == 0) break;
				else r += re;
			}
		} finally {
			pos += r;
		}
	}
	
	public int read () throws IOException {
		int i = input.read();
		pos += 1;
		return i;
	}
	
	public long skip (long n) throws IOException {
		byte[] scan = new byte[512];
		long a = n / 512; int b = (int)(n % 512);
		long re = 0;
		while (a-- > 0) re += input.read(scan, 0, 512);
		re += input.read(scan, 0, b);
		/*long re = stream.skip(n);*/
		pos += re;
		return re;
	}
	
	public void close () throws IOException {
		input.close();
	}
	
	public long position() { return pos; }
	
	public void seek (long position) throws IOException {
		if (position < pos) rewind();
		if (position > pos) {
			skip(position - pos);
		}
	}
}
