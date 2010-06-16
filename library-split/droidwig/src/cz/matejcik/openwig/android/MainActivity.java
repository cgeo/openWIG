/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.matejcik.openwig.android;

import android.app.*;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author matejcik
 */
public class MainActivity extends ListActivity {

	private ArrayAdapter<String> adapter;

	private ArrayList<File> filelist = new ArrayList<File>();

	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
		public void onItemClick (AdapterView<?> av, View view, int pos, long id) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			//i.setClass(MainActivity.this, CartDetailsActivity.class);
			Uri uri = Uri.fromFile(filelist.get(pos));
			Uri urii = Uri.withAppendedPath(uri, ".blabla");
			i.setDataAndType(urii, "bla/bla");
			startActivity(i);
		}
	};
	
	@Override
	public void onCreate (Bundle icicle) {
		super.onCreate(icicle);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		setListAdapter(adapter);
		getListView().setTextFilterEnabled(true);
		getListView().setOnItemClickListener(listener);
	}

	@Override
	public void onResume () {
		super.onResume();
		File file = new File("/sdcard");
		filelist.clear();
		adapter.clear();
		File[] list = file.listFiles();
		for (File item : list) {
			if (item.getName().toLowerCase().endsWith(".gwc")) {
				filelist.add(item);
				adapter.add(item.getName());
			}
		}
	}
}
