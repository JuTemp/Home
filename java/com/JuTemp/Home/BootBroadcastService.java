package com.JuTemp.Home;
import android.app.*;
import android.os.*;
import android.content.*;
import android.preference.PreferenceManager;

public class BootBroadcastService extends Service
{

	BootBroadcastService This=this;

	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(This);
		boolean flag=sp.getBoolean(ApplJuTemp.BOOTALARMMEMO_FLAG, false);
		if (!flag) return;
		String content=sp.getString(ApplJuTemp.BOOTALARMMEMO_CONTENT, "");
		
		Intent it = new Intent(This, BootAlarmMemoJuTemp.class);
		it.putExtra("content",content);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		This.startActivity(it);
		
		This.stopSelf();
	}



}
