package gwc;

import java.io.*;

public class GwcInput {
	private InputStream stream;
	private long pos = 0;
	
	public GwcInput(InputStream is) {
		stream = is;
	}
	
	public short readShort() throws IOException {
		byte[] r = new byte[2];
		pos += stream.read(r);
		return (short)((r[1] << 8) | (r[0] & 0xff));
	}
	
	public int readInt() throws IOException {
		byte[] r = new byte[4];
		pos += stream.read(r);
		return (((r[3] & 0xff) << 24) | ((r[2] & 0xff) << 16) |
			((r[1] & 0xff) << 8) | (r[0] & 0xff));
	}
	
	public String readString() throws IOException {
		StringBuffer sb = new StringBuffer();
		int b = stream.read(); pos++;
		while (b > 0) {
			sb.append((char)b);
			b = stream.read(); pos++;
		}
		return sb.toString();
	}
	
	public long readLong() throws IOException {
		byte[] r = new byte[8];
		pos += stream.read(r);
		return (((long)(r[7] & 0xff) << 56) |
			((long)(r[6] & 0xff) << 48) |
			((long)(r[5] & 0xff) << 40) |
			((long)(r[4] & 0xff) << 32) |
			((long)(r[3] & 0xff) << 24) |
			((long)(r[2] & 0xff) << 16) |
			((long)(r[1] & 0xff) <<  8) |
			((long)(r[0] & 0xff)));
	}
	
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}
	
	public int read (byte[] b) throws IOException {
		int re = stream.read(b);
		pos += re;
		return re;
	}
	
	public int read () throws IOException {
		pos++;
		return stream.read();
	}
	
	public long skip (long n) throws IOException {
		long re = stream.skip(n);
		pos += re;
		return re;
	}
	
	public void close () throws IOException {
		stream.close();
	}
	
	public long position() { return pos; }
	
	public void pseudoSeek (long position) throws IOException {
		if (position < pos) throw new IOException("can't seek backwards, sorry :e(");
		if (position > pos) {
			long skip = stream.skip(position - pos);
			pos += skip;
		}
	}
}
