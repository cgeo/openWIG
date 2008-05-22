package gwc;

import java.io.*;
import javax.microedition.rms.*;

public class CartridgeFile {
	
	private static final byte[] CART_ID = { 0x02, 0x0a, 0x43, 0x41, 0x52, 0x54, 0x00 };
			// 02 0a CART 00
	
	private static final String STORENAME = "_gwc_cache";
	
	private GwcInput source;
	private RecordStore store;
	
	private int files;
	private int[] offsets;
	private int[] ids;
	private int[] records;
	
	public byte[] bytecode;
	
	public double latitude, longitude;
	public String type, member, name, description, startdesc, version, author, url, device, code;
	public int iconId, splashId;
	
	private CartridgeFile(InputStream src) {
		source = new GwcInput(src);
	}
	
	private boolean fileOk () throws IOException {
		byte[] buf = new byte[CART_ID.length];
		source.read(buf);
		for (int i = 0; i < buf.length; i++) if (buf[i]!=CART_ID[i]) return false;
		return true;
	}
	
	public static CartridgeFile read(InputStream what)
	throws IOException, RecordStoreException {
		CartridgeFile cf = new CartridgeFile(what);
		if (!cf.fileOk()) return null;
		
		cf.scanOffsets();
		cf.scanHeader();
		
		try { RecordStore.deleteRecordStore(STORENAME); } catch (RecordStoreException e) { }
		cf.store = RecordStore.openRecordStore(STORENAME, true);
		
		cf.loadBytecode();
		
		for (int i = 1; i < cf.files; i++) {
			int mo = Integer.MAX_VALUE;
			int id = 0;
			for (int j = 1; j < cf.files; j++) {
				if (cf.records[j] == 0 && cf.offsets[j] < mo) {
					mo = cf.offsets[j];
					id = j;
				}
			}
			cf.loadFile(id);
		}
		
		return cf;
	}
	
	private void scanOffsets () throws IOException {
		files = source.readShort();
		offsets = new int[files];
		ids = new int[files];
		records = new int[files];
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
	
	private void loadBytecode () throws IOException {
		source.pseudoSeek(offsets[0]);
		int len = source.readInt();
		byte[] file = new byte[len];
		source.read(file);
		try {
			int recno = store.addRecord(file, 0, len);
			records[0] = recno;
		} catch (RecordStoreFullException e) {
			gui.Midlet.error("Record Store full. Sorry, you can't load this cartridge :/");
		} catch (RecordStoreException e) { /* mor a cholera na tebe! */ }
	}
	
	private void loadFile (int id) throws IOException {
		source.pseudoSeek(offsets[id]);
		int a = source.read();
		if (a != 1) { // deleted object 
			records[id] = -1;
			return;
		}
		int type = source.readInt(); // we don't need this?
		int len = source.readInt();
		byte[] file = new byte[len];
		source.read(file);
		try {
			int recno = store.addRecord(file, 0, len);
			records[id] = recno;
		} catch (RecordStoreFullException e) {
			gui.Midlet.error("Record Store full. Sorry, you can't load this cartridge :/");
		} catch (RecordStoreException e) { /* mor a cholera na tebe! */
			gui.Midlet.error("Weird thing happened: "+e.getMessage());
		}
	}
	
	public byte[] getBytecode () throws IOException {
		return getFile(0);
	}
	
	public byte[] getFile (int id) throws IOException {
		if (id < 0) // invalid, apparently
			return null;
		
		for (int i = 0; i < ids.length; i++)
			if (ids[i] == id) {
				id = i;
				break;
			}
		try {
			return store.getRecord(records[id]);
		} catch (RecordStoreException e) {
			gui.Midlet.error("Weird thing happened: "+e.getMessage());
			return null;
		}
	}
}
