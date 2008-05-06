package gwc;

import java.io.*;

public class CartridgeFile {
	
	private static final byte[] CART_ID = { 0x02, 0x0a, 0x43, 0x41, 0x52, 0x54, 0x00 };
			// 02 0a CART 00
	
	private GwcInput source;
	
	private int files;
	private int[] offsets;
	
	public double latitude, longitude;
	public String type, member, name, description, startdesc, version, author, url, device, code;
	
	private CartridgeFile() { }
	
	public static CartridgeFile read(InputStream what)
	throws IOException {
		CartridgeFile cf = new CartridgeFile();
		if (what.markSupported()) what.mark(Integer.MAX_VALUE);
		cf.source = new GwcInput(what);
		
		byte[] buf = new byte[CART_ID.length];
		cf.source.stream.read(buf);
		for (int i = 0; i < buf.length; i++) if (buf[i]!=CART_ID[i]) return null;
		
		cf.files = cf.source.readUnsignedShort();
		cf.offsets = new int[cf.files];
		for (int i = 0; i < cf.files; i++) {
			int id = cf.source.readUnsignedShort();
			int ofs = cf.source.readInt();
			if (ofs < 0) throw new IOException("matejciku jsi debil");
			cf.offsets[i] = (int)ofs;
		}
		
		int headerlen = cf.source.readInt();
		byte[] header = new byte[headerlen];
		cf.source.stream.read(header);
		GwcInput dis = new GwcInput(new ByteArrayInputStream(header));
		cf.latitude = dis.readDouble();
		cf.longitude = dis.readDouble();
		dis.stream.skip(8 /*zeroes*/ + 4+4+2+2 /* weird values */);
		cf.type = dis.readString();
		cf.member = dis.readString();
		dis.stream.skip(4+4); // weird values
		cf.name = dis.readString();
		dis.readString(); // GUID
		cf.description = dis.readString();
		cf.startdesc = dis.readString();
		cf.version = dis.readString();
		cf.author = dis.readString();
		cf.url = dis.readString();
		cf.device = dis.readString();
		dis.stream.skip(4); // weird value
		cf.code = dis.readString();
		
		return cf;
	}
	
	public InputStream getBytecode () throws IOException {
		// bytecode is object 0
		source.stream.reset();
		source.stream.skip(offsets[0]);
		int len = source.readInt();
		byte[] file = new byte[len];
		source.stream.read(file);
		
		return new ByteArrayInputStream(file);
	}
	
	public InputStream getFile (int id) throws IOException {
		if (id < 1 || id >= offsets.length) // that's either invalid or MY FREAKIN BYTECODE, HANDS OFF!!
			return null;
		
		source.stream.reset();
		source.stream.skip(offsets[id]);
		int a = source.stream.read();
		if (a != 1) // that's a deleted object
			return null;
		int type = source.readInt(); // not needed?
		int len = source.readInt();
		byte[] file = new byte[len];
		source.stream.read(file);
		return new ByteArrayInputStream(file);
	}
}
