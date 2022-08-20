package com.JuTemp.Home.util;

import android.app.*;
import android.content.*;
import android.net.*;
import android.widget.*;

import com.JuTemp.Home.ApplJuTemp;

public class IntentJuTempUtil {
	public static void intentUri(Activity This, String str) {
		try {
			Intent intent = new Intent();
			intent.setData(Uri.parse(str));
			if (str.startsWith("http://") || str.startsWith("https://"))
				intent.setClassName("mark.via", "mark.via.Shell");
			This.startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(This, "Isn't Correct URL.", Toast.LENGTH_LONG).show();
		}
	}

	public static void intentFileSimple(Activity This) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		This.startActivityForResult(intent, ((ApplJuTemp)This.getApplication()).FILE_SELECTOR_REQUEST_CODE);
	}

/*	public static void intentFileMulti(Activity This) {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), FsActivity.class);
		intent.putExtra(FileSelectConstant.SELECTOR_REQUEST_CODE_KEY, FileSelectConstant.SELECTOR_MODE_FILE);
		intent.putExtra(FileSelectConstant.SELECTOR_IS_MULTIPLE, true);
		This.startActivityForResult(intent, FILE_SELECTOR_REQUEST_CODE);
	}

	public static void intentFolderSimple(Activity This) {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), FsActivity.class);
		intent.putExtra(FileSelectConstant.SELECTOR_REQUEST_CODE_KEY, FileSelectConstant.SELECTOR_MODE_FOLDER);
		This.startActivityForResult(intent, FILE_SELECTOR_REQUEST_CODE);
	}

	public static void intentFolderMulti(Activity This) {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), FsActivity.class);
		intent.putExtra(FileSelectConstant.SELECTOR_REQUEST_CODE_KEY, FileSelectConstant.SELECTOR_MODE_FOLDER);
		intent.putExtra(FileSelectConstant.SELECTOR_IS_MULTIPLE, true);
		This.startActivityForResult(intent, FILE_SELECTOR_REQUEST_CODE);
	}*/
}
