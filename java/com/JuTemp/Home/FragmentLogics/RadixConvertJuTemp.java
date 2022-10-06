package com.JuTemp.Home.FragmentLogics;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.JuTemp.Home.R;
import com.JuTemp.Home.FragmentFramework.FragmentLogicJuTemp;

public class RadixConvertJuTemp extends FragmentLogicJuTemp {

    @Override
    public void mainLogic(Activity ThisActivity, FragmentLogicJuTemp fragmentLogic, View view) {
        super.mainLogic(ThisActivity, fragmentLogic, view);

        requestFocus(ThisActivity, view.findViewById(R.id.radixconvert_dec));

        view.findViewById(R.id.radixconvert_exec).setOnClickListener(v -> {
            View viewFocusing = ThisActivity.getWindow().getDecorView().findFocus();
            if (!(viewFocusing instanceof EditText)) return;
            switch (viewFocusing.getId()) {
                case R.id.radixconvert_bin:
                    exec(((EditText) viewFocusing).getText().toString().trim(), 2);
                    break;
                case R.id.radixconvert_oct:
                    exec(((EditText) viewFocusing).getText().toString().trim(), 8);
                    break;
                case R.id.radixconvert_dec:
                    exec(((EditText) viewFocusing).getText().toString().trim(), 10);
                    break;
                case R.id.radixconvert_hex:
                    exec(((EditText) viewFocusing).getText().toString().trim(), 16);
                    break;
                default:
                    break;
            }
        });
    }

    private void exec(String str, int r) {
        try {
            int num = Integer.parseInt(str, r);
            if (r != 2)
                ((AppCompatEditText) view.findViewById(R.id.radixconvert_bin)).setText(Integer.toBinaryString(num));
            if (r != 8)
                ((AppCompatEditText) view.findViewById(R.id.radixconvert_oct)).setText(Integer.toOctalString(num));
            if (r != 10)
                ((AppCompatEditText) view.findViewById(R.id.radixconvert_dec)).setText(String.valueOf(num));
            if (r != 16)
                ((AppCompatEditText) view.findViewById(R.id.radixconvert_hex)).setText(Integer.toHexString(num));
        } catch (Exception e) {
            Toast.makeText(ThisActivity,Re.getString(R.string.radixconvert_error) , Toast.LENGTH_LONG).show();
        }
    }
}
