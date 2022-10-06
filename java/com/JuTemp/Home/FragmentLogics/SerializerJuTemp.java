package com.JuTemp.Home.FragmentLogics;
import android.app.*;
import android.view.*;
import android.widget.*;
import com.JuTemp.Home.*;
import com.JuTemp.Home.FragmentFramework.FragmentLogicJuTemp;
import com.JuTemp.Home.util.*;

public class SerializerJuTemp extends FragmentLogicJuTemp
{
	@Override
	public void mainLogic(final Activity ThisActivity, final FragmentLogicJuTemp fragmentLogic, final View view)
	{
		super.mainLogic(ThisActivity,fragmentLogic,view);

		requestFocus(ThisActivity, view.findViewById(R.id.serializer_ser));

		view.findViewById(R.id.serializer_exec).setOnClickListener(p1 -> {
			View viewFocusing=ThisActivity.getWindow().getDecorView().findFocus();
			if (!(viewFocusing instanceof EditText)) return;
			switch (viewFocusing.getId())
			{
				case R.id.serializer_ser: // want to serialize
					Object objectTryToDeserialize=ObjectSerializerJuTemp.deserialize(ThisActivity, ((EditText)viewFocusing).getText().toString());
					if (objectTryToDeserialize == null)
					{
						Toast.makeText(ThisActivity, Re.getString(R.string.serializer_deser_error), Toast.LENGTH_LONG).show();
						break;
					}
					((EditText)view.findViewById(R.id.serializer_deser)).setText(objectTryToDeserialize.toString());
					break;
				case R.id.serializer_deser: // want to deserialize
					((EditText)view.findViewById(R.id.serializer_ser)).setText(ObjectSerializerJuTemp.serialize(ThisActivity, ((EditText)viewFocusing).getText().toString()));
					break;
				default:
					break;
			}

		});
	}


}
