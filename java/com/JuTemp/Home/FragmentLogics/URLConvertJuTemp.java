package com.JuTemp.Home.FragmentLogics;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.JuTemp.Home.R;
import com.JuTemp.Home.util.FragmentLogicJuTemp;
import com.JuTemp.Home.util.HTTPJuTemp;

public class URLConvertJuTemp extends FragmentLogicJuTemp {
    @Override
    public void mainLogic(Activity ThisActivity, FragmentLogicJuTemp fragmentLogic, View view) {
        super.mainLogic(ThisActivity, fragmentLogic, view);

        requestFocus(ThisActivity, view.findViewById(R.id.urlconvertTextOriginal));

        view.findViewById(R.id.urlconvertExec).setOnClickListener(p1 -> {
            View viewFocusing = ThisActivity.getWindow().getDecorView().findFocus();
            if (!(viewFocusing instanceof EditText)) return;
            switch (viewFocusing.getId()) {
                case R.id.urlconvertTextOriginal: // want to encode
                    String objectTryToEncode = null;
                    try {
                        objectTryToEncode = HTTPJuTemp.encode(((EditText) viewFocusing).getText().toString());
                    } catch (Exception e) {
                        Toast.makeText(ThisActivity, Re.getString(R.string.urlconvert_encode_error), Toast.LENGTH_LONG).show();
                        break;
                    }
                    ((EditText) view.findViewById(R.id.urlconvertTextEncoded)).setText(objectTryToEncode);
                    break;
                case R.id.urlconvertTextEncoded: // want to decode
                    String objectTryToDecode = null;
                    try {
                        objectTryToDecode = HTTPJuTemp.decode(((EditText) viewFocusing).getText().toString());
                    } catch (Exception e) {
                        Toast.makeText(ThisActivity, Re.getString(R.string.urlconvert_decode_error), Toast.LENGTH_LONG).show();
                        break;
                    }
                    ((EditText) view.findViewById(R.id.urlconvertTextOriginal)).setText(objectTryToDecode);
                    break;
                default:
                    break;
            }

        });

    }
}
