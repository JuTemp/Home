package com.JuTemp.Home.util;

import android.content.*;
import android.widget.Toast;

public class ClipJuTemp
{
	public static void setClipboard(Context This,String str)
	{
		ClipboardManager cm=(ClipboardManager)This.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData cd=ClipData.newPlainText("ClipboardJuTemp",str);
		Toast.makeText(This, str, Toast.LENGTH_LONG).show();
		cm.setPrimaryClip(cd);
	}
	public static String getClipboard(Context This)
	{
		ClipboardManager cm=(ClipboardManager)This.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData cd=cm.getPrimaryClip();
		if (cd==null) return null;
		ClipData.Item cdi=cd.getItemAt(0);
		return cdi.getText().toString();
	}
}
