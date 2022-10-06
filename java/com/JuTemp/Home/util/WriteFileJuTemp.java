package com.JuTemp.Home.util;

import java.io.*;
import android.os.*;
import com.JuTemp.Home.*;

public final class WriteFileJuTemp
{
	public static void write(String path,String fileName,String content) throws Exception
	{
		File saveFile = new File(Environment.getExternalStorageDirectory() + path, fileName);
		OutputStream out = new FileOutputStream(saveFile);
		out.write(content.getBytes());
		out.close();
	}
}
