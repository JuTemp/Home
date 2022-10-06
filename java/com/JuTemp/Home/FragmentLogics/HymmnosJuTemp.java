package com.JuTemp.Home.FragmentLogics;

import android.app.*;
import android.graphics.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.JuTemp.Home.*;
import com.JuTemp.Home.FragmentFramework.FragmentLogicJuTemp;

public class HymmnosJuTemp extends FragmentLogicJuTemp
{
	static HymmnosJuTemp This=null;
	static Activity ThisActivity=null;
	static View view=null;

	@Override
	public void mainLogic(final Activity ThisActivity, final FragmentLogicJuTemp fragmentLogic, final View view)
	{
		this.This = (HymmnosJuTemp) fragmentLogic;
		this.ThisActivity = ThisActivity;
		this.view = view;
		
		super.requestFocus(ThisActivity, (EditText)view.findViewById(R.id.hymmnosTextIn));
		
		((TextView)view.findViewById(R.id.hymmnosTextOut)).setTypeface(Typeface.createFromAsset(ThisActivity.getAssets(),"ttf/hymmnos.ttf"));
		
		((Button)view.findViewById(R.id.hymmnosTrans)).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					((TextView)view.findViewById(R.id.hymmnosTextOut)).setText(((TextView)view.findViewById(R.id.hymmnosTextIn)).getText().toString());
				}
			});
	}
	
}
