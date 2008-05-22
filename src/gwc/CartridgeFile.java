package gwc;

import gui.Midlet;
import java.io.*;
import javax.microedition.io.*;

public class CartridgeFile {
	
	private static final byte[] CART_ID = { 0x02, 0x0a, 0x43, 0x41, 0x52, 0x54, 0x00 };
			// 02 0a CART 00
	
	private GwcInput source;
	private String connectionUrl;
	
	private int files;
	private int[] offsets;
	private int[] ids;
	
	public byte[] bytecode;
	
	public double latitude, longitude;
	public String type, member, name, description, startdesc, version, author, url, device, code;
	public int iconId, splashId;
	
	private CartridgeFile(String connection) {
		connectionUrl = connection;
	}
	
	private void resetSource()
	throws Exception {
		System.out.println("resetting source");
		if (source != null) source.close();
		source = new GwcInput(Midlet.connect(connectionUrl));
	}
	
	private boolean fileOk () throws IOException {
		byte[] buf = new byte[CART_ID.length];
		source.read(buf);
		for (int i = 0; i < buf.length; i++) if (buf[i]!=CART_ID[i]) return false;
		return true;
	}
	
	public static CartridgeFile read(String what)
	throws Exception {
		CartridgeFile cf = new CartridgeFile(what);
		cf.resetSource();
		if (!cf.fileOk()) return null;
		
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
