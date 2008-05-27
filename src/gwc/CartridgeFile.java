package gwc;

import java.io.*;
import javax.microedition.io.*;
import javax.microedition.io.file.FileConnection;

public class CartridgeFile {
	
	private static final byte[] CART_ID = { 0x02, 0x0a, 0x43, 0x41, 0x52, 0x54, 0x00 };
			// 02 0a CART 00
	
	private GwcInput source;
	private String connectionUrl;
	private FileConnection file;
	
	private int files;
	private int[] offsets;
	private int[] ids;
	
	public byte[] bytecode;
	
	public double latitude, longitude;
	public String type, member, name, description, startdesc, version, author, url, device, code;
	public int iconId, splashId;
	
	private CartridgeFile() { }
	
	private void resetSource()
	throws Exception {
		if (source != null) source.close();
		if (file != null) 
			source = new GwcInput(file.openInputStream());
		else
			source = new GwcInput(getClass().getResourceAsStream(connectionUrl));
	}
	
	private boolean fileOk () throws IOException {
		byte[] buf = new byte[CART_ID.length];
		source.read(buf);
		for (int i = 0; i < buf.length; i++) if (buf[i]!=CART_ID[i]) return false;
		return true;
	}
	
	public static CartridgeFile read(String what)
	throws Exception {
		CartridgeFile cf = new CartridgeFile();
		if (what.startsWith("resource:")) {
			String url = what.substring(9);
			if (cf.getClass().getResourceAsStream(url) == null)
				throw new Exception("resource not found");
			cf.connectionUrl = url;
		} else if (what.startsWith("file:")) {
			cf.file = (FileConnection)Connector.open(what);
		} else {
			throw new IllegalArgumentException("invalid connection string");
		}
		
		cf.resetSource();
		if (!cf.fileOk()) throw new Exception("invalid cartridge file");
		
		cf.scanOffsets();
		cf.scanHeader();
			
		return cf;
	}
	
	private void scanOffsets () throws IOException {
		files = source.readShort();
		offsets = new int[files];
		ids = new int[files];
		for (int i = 0; i < files; i++) {
			ids[i] = source.readShort();
			offsets[i] = source.readInt();
		}
	}
	
	private void scanHeader () throws IOException {
		int headerlen = source.readInt();
		byte[] header = new byte[headerlen];
		source.read(header);
		
		GwcInput dis = new GwcInput(new ByteArrayInputStream(header));
		latitude = dis.readDouble();
		longitude = dis.readDouble();
		dis.skip(8); // zeroes
		dis.skip(4+4); // unknown long values
		iconId = dis.readShort();
		splashId = dis.readShort();
		type = dis.readString();
		member = dis.readString();
		dis.skip(4+4); // unknown long values
		name = dis.readString();
		dis.readString(); // GUID
		description = dis.readString();
		startdesc = dis.readString();
		version = dis.readString();
		author = dis.readString();
		url = dis.readString();
		device = dis.readString();
		dis.skip(4); // unknown long value
		code = dis.readString();
	}
	
	public byte[] getBytecode () throws Exception {
		if (source.position() > offsets[0]) resetSource();
		source.pseudoSeek(offsets[0]);
		int len = source.readInt();
		byte[] file = new byte[len];
		source.read(file);
		return file;
	}

	private int lastId = -1;
	private byte[] lastFile = null;
	
	public byte[] getFile (int id) throws Exception {
		if (id == lastId) return lastFile;
		if (id < 1) // invalid, apparently. or bytecode - lookie no touchie
			return null;
		
		for (int i = 0; i < ids.length; i++)
			if (ids[i] == id) {
				id = i;
				break;
			}
		
		if (source.position() > offsets[id]) resetSource();
		source.pseudoSeek(offsets[id]);
		int a = source.read();
		if (a != 1) { // deleted object 
			return null;
		}
		int type = source.readInt(); // we don't need this?
		int len = source.readInt();
		byte[] file = new byte[len];
		source.read(file);
		
		lastId = id;
		lastFile = file;
		return file;
	}
}
