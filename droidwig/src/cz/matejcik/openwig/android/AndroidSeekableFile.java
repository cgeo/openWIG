package cz.matejcik.openwig.android;

import cz.matejcik.openwig.platform.SeekableFile;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AndroidSeekableFile implements SeekableFile {

	private RandomAccessFile source;

	public AndroidSeekableFile(RandomAccessFile raf) {
		source = raf;

	}

	public void seek (long pos) throws IOException {
		source.seek(pos);
	}

	public long position () throws IOException {
		return source.getFilePointer();
	}

	public long skip (long what) throws IOException {
		return source.skipBytes((int)what);
	}

	public short readShort () throws IOException {
		return Short.reverseBytes(source.readShort());
	}

	public int readInt () throws IOException {
		return Integer.reverseBytes(source.readInt());
	}

	public double readDouble () throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	public long readLong () throws IOException {
		return Long.reverseBytes(source.readLong());
	}

	public void readFully (byte[] buf) throws IOException {
		source.readFully(buf);
	}

	public String readString () throws IOException {
		StringBuffer sb = new StringBuffer();
		int b = source.read();
		while (b > 0) {
			sb.append((char)b);
			b = source.read();
		}
		return sb.toString();
	}

	public int read () throws IOException {
		return source.read();
	}

}
