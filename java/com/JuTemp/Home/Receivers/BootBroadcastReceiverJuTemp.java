package com.JuTemp.Home.Receivers;

import android.content.*;

import com.JuTemp.Home.Services.BootBroadcastService;

public class BootBroadcastReceiverJuTemp extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Intent it = new Intent(context, BootBroadcastService.class);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startService(it);
	}

}
