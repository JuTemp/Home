package com.JuTemp.Home.FragmentLogics;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.JuTemp.Home.R;
import com.JuTemp.Home.util.Base64JuTempUtil;
import com.JuTemp.Home.FragmentFramework.FragmentLogicJuTemp;

public class Base64JuTemp extends FragmentLogicJuTemp {

    @Override
    public void mainLogic(Activity ThisActivity, FragmentLogicJuTemp fragmentLogic, View view) {
        super.mainLogic(ThisActivity, fragmentLogic, view);

        requestFocus(ThisActivity,view.findViewById(R.id.base64Content));

        view.findViewById(R.id.base64Exec).setOnClickListener(p1 -> {
            View viewFocusing = ThisActivity.getWindow().getDecorView().findFocus();
            if (!(viewFocusing instanceof EditText)) return;
            switch (viewFocusing.getId()) {
                case R.id.base64Content: // want to encode
                    ((EditText) view.findViewById(R.id.base64Encoded)).setText(Base64JuTempUtil.encode(((EditText) viewFocusing).getText().toString()));
                    break;
                case R.id.base64Encoded: // want to decode
                    String objectTryToDecode = Base64JuTempUtil.decode(((EditText) viewFocusing).getText().toString());
                    if (objectTryToDecode == null) {
                        Toast.makeText(ThisActivity, Re.getString(R.string.base64_decode_error), Toast.LENGTH_LONG).show();
                        break;
                    }
                    ((EditText) view.findViewById(R.id.base64Content)).setText(objectTryToDecode);
                    break;
                default:
                    break;
            }

        });

    }
}
