package com.JuTemp.Home.Activities;
import android.app.*;
import android.os.*;

public class BootAlarmMemoJuTemp extends Activity
{
	BootAlarmMemoJuTemp This=this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		new AlertDialog.Builder(This)
			.setMessage(This.getIntent().getStringExtra("content"))
			.create().show();
	}
	
}
