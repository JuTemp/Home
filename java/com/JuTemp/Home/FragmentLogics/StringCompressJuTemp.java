package com.JuTemp.Home.FragmentLogics;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.JuTemp.Home.R;
import com.JuTemp.Home.util.FragmentLogicJuTemp;
import com.JuTemp.Home.util.LZ78JuTemp;
import com.JuTemp.Home.util.ObjectSerializerJuTemp;

import java.util.Objects;

public class StringCompressJuTemp extends FragmentLogicJuTemp {
    @Override
    public void mainLogic(Activity ThisActivity, FragmentLogicJuTemp fragmentLogic, View view) {
        super.mainLogic(ThisActivity, fragmentLogic, view);

        requestFocus(ThisActivity,view.findViewById(R.id.stringcompressTextLong));

        view.findViewById(R.id.stringcompressExec).setOnClickListener(p1 -> {
            View viewFocusing = ThisActivity.getWindow().getDecorView().findFocus();
            if (!(viewFocusing instanceof EditText)) return;
            switch (viewFocusing.getId()) {
                case R.id.stringcompressTextLong: // want to encode
                    String objectTryToEncode=LZ78JuTemp.encode(((EditText) viewFocusing).getText().toString());
                    if (objectTryToEncode==null)
                    {
                        Toast.makeText(ThisActivity, Re.getString(R.string.stringcompress_encode_error), Toast.LENGTH_LONG).show();
                        break;
                    }
                    ((EditText) view.findViewById(R.id.stringcompressTextShort)).setText(objectTryToEncode);
                    break;
                case R.id.stringcompressTextShort: // want to decode
                    String objectTryToDecode = LZ78JuTemp.decode(((EditText) viewFocusing).getText().toString());
                    if (objectTryToDecode == null) {
                        Toast.makeText(ThisActivity, Re.getString(R.string.stringcompress_decode_error), Toast.LENGTH_LONG).show();
                        break;
                    }
                    ((EditText) view.findViewById(R.id.stringcompressTextLong)).setText(objectTryToDecode);
                    break;
                default:
                    break;
            }

        });

    }
}
