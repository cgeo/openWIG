package gwc;

import java.io.*;

public class CartridgeFile {
	
	private static final byte[] CART_ID = { 0x02, 0x0a, 0x43, 0x41, 0x52, 0x54, 0x00 };
			// 02 0a CART 00
	
	private GwcInput source;
	
	private int files;
	private int[] offsets;
	private int[] ids;
	
	public double latitude, longitude;
	public String type, member, name, description, startdesc, version, author, url, device, code;
	public int iconId, splashId;
	
	private CartridgeFile() { }
	
	public static CartridgeFile read(InputStream what)
	throws IOException {
		CartridgeFile cf = new CartridgeFile();
		if (what.markSupported()) what.mark(Integer.MAX_VALUE);
		cf.source = new GwcInput(what);
		
		byte[] buf = new byte[CART_ID.length];
		cf.source.stream.read(buf);
		for (int i = 0; i < buf.length; i++) if (buf[i]!=CART_ID[i]) return null;
		
		cf.files = cf.source.readShort();
		cf.offsets = new int[cf.files];
		cf.ids = new int[cf.files];
		for (int i = 0; i < cf.files; i++) {
			cf.ids[i] = cf.source.readShort();
			cf.offsets[i] = cf.source.readInt();
		}
		
		int headerlen = cf.source.readInt();
		byte[] header = new byte[headerlen];
		cf.source.stream.read(header);
		GwcInput dis = new GwcInput(new ByteArrayInputStream(header));
		cf.latitude = dis.readDouble();
		cf.longitude = dis.readDouble();
		dis.stream.skip(8); // zeroes
		dis.stream.skip(4+4); // unknown long values
		cf.iconId = dis.readShort();
		cf.splashId = dis.readShort();
		cf.type = dis.readString();
		cf.member = dis.readString();
		dis.stream.skip(4+4); // unknown long values
		cf.name = dis.readString();
		dis.readString(); // GUID
		cf.description = dis.readString();
		cf.startdesc = dis.readString();
		cf.version = dis.readString();
		cf.author = dis.readString();
		cf.url = dis.readString();
		cf.device = dis.readString();
		dis.stream.skip(4); // unknown long value
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
		if (id < 1) // hands off my bytecode!
			return null;
		
		for (int i = 0; i < ids.length; i++)
			if (ids[i] == id) {
				id = i;
				break;
			}
		
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
