package com.JuTemp.Home.util;

import android.app.*;
import android.content.*;
import android.content.res.Resources;
import android.os.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import com.JuTemp.Home.*;

import java.util.List;
import java.util.Map;

public abstract class FragmentLogicJuTemp
{
	public Activity ThisActivity=null;
	public FragmentLogicJuTemp This=null;
	public View view=null;
	public Resources Re=null;
	public void mainLogic(final Activity ThisActivity,final FragmentLogicJuTemp fragmentLogic,final View view)
	{
		this.ThisActivity=ThisActivity;
		this.This=fragmentLogic;
		this.view=view;
		Re=ThisActivity.getResources();
	}
	public void onResume() {}

	public void importJson(String str) {}
	public String exportJson() { return null; }
	public void saveData(List<Map<String, Object>> data) {}
	public static void requestFocus(final Activity ThisActivity, final EditText editText)
	{
		ApplJuTemp.requestFocus(ThisActivity,editText);
	}
	public void onActivityResult(final Activity ThisActivity,final int requestCode,final int resultCode,final Intent intent){}

	public void actionbarMenuItemClickListener(MenuItem item){}
}
