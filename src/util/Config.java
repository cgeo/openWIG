package util;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.rms.*;

public class Config {
	private Hashtable values;
	private RecordStore store;
	
	public Config (String storename)
	throws RecordStoreException {
		store = RecordStore.openRecordStore(storename, true);
		values = new Hashtable();
		if (store.getSize() > 0) fetchValues();
	}
	
	private void fetchValues ()
	throws RecordStoreException {
		byte[] r = store.getRecord(0);
		String record = new String(r);
		int si = 0;
		int idx;
		while ((idx = record.indexOf('\n', si)) > -1) {
			int sep = record.indexOf(':', si);
			try {
			values.put(record.substring(si, sep), record.substring(sep+1,idx));
			} catch (IndexOutOfBoundsException e) { /* oh well */ }
			si = idx+1;
		}
	}
	
	public void commit ()
	throws RecordStoreException {
		StringBuffer rep = new StringBuffer();
		Enumeration k = values.keys();
		while (k.hasMoreElements()) {
			String key = (String)k.nextElement();
			rep.append(key);
			rep.append(':');
			rep.append((String)values.get(key));
			rep.append('\n');
		}
		byte[] data = rep.toString().getBytes();
		if (store.getSize() > 0) {
			store.setRecord(0, data, 0, data.length);
		} else {
			store.addRecord(data, 0, data.length);
		}
	}
	
	public void set (String key, String value) {
		values.put(key, value);
	}
	
	public String get (String key) {
		return (String)values.get(key);
	}
}