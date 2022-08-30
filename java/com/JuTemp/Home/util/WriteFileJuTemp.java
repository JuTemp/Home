package com.JuTemp.Home.util;

import java.io.*;
import android.os.*;
import com.JuTemp.Home.*;

public final class WriteFileJuTemp
{
	public static void write(String fileName,String context) throws Exception
	{
		File saveFile = new File(Environment.getExternalStorageDirectory() + "/!temp/temp/", fileName);
		OutputStream out = new FileOutputStream(saveFile);
		out.write(context.getBytes());
		out.close();
	}
}
