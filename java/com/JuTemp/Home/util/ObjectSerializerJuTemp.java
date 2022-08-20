package com.JuTemp.Home.util;

import android.widget.*;
import android.app.*;
import java.io.*;

public class ObjectSerializerJuTemp
{
	public static Object deserialize(Activity This, String str)
	{
		try
		{
			return ObjectSerializer.deserialize(str);
		}
		catch (Exception e)
		{
			Toast.makeText(This, e.toString(), Toast.LENGTH_LONG).show();
		}
		return null;
	}
	
	public static String serialize(Activity This,Serializable obj)
	{
		try
		{
			return ObjectSerializer.serialize(obj);
		}
		catch(Exception e)
		{
			Toast.makeText(This, e.toString(), Toast.LENGTH_LONG).show();
		}
		return null;
	}
}
