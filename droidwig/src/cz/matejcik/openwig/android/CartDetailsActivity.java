package cz.matejcik.openwig.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cz.matejcik.openwig.formats.CartridgeFile;
import cz.matejcik.openwig.platform.*;
import java.io.*;

public class CartDetailsActivity extends Activity implements TmpBrowserSource {

	@Override
	public void onCreate (Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.cart_details);
		Intent why = getIntent();
		if (why != null) try {
			RandomAccessFile raf = new RandomAccessFile(why.getData().getPath(), "r");
			CartridgeFile cf = CartridgeFile.read(this, new AndroidSeekableFile(raf));
			TextView t = (TextView)findViewById(R.id.cart_title);
			t.setText(cf.name);
			t = (TextView)findViewById(R.id.cart_description);
			t.setText(cf.description);
			byte[] b = cf.getFile(cf.splashId);
			Bitmap splash = BitmapFactory.decodeByteArray(b, 0, b.length);
			ImageView iv = (ImageView)findViewById(R.id.cart_splash);
			iv.setImageBitmap(splash);
		} catch (IOException e) {
			Toast.makeText(this, e.toString(), 5000).show();
			// yada yada TODO
		}
	}

	public FileHandle getSyncFile () throws IOException {
		return new AndroidFileHandle(new File("/sdcard/save.gws"));
	}
}
