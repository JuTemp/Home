package com.JuTemp.Home.FragmentLogics;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.JuTemp.Home.R;
import com.JuTemp.Home.FragmentFramework.FragmentLogicJuTemp;
import com.JuTemp.Home.util.WriteFileJuTemp;

import java.util.Objects;

public class MemoTextJuTemp extends FragmentLogicJuTemp {

    AppCompatEditText titleEd=null,contentEd=null;

    @Override
    public void mainLogic(Activity ThisActivity, FragmentLogicJuTemp fragmentLogic, View view) {
        super.mainLogic(ThisActivity, fragmentLogic, view);

        titleEd=view.findViewById(R.id.memotext_title);
        contentEd=view.findViewById(R.id.memotext_content);
    }

    @Override
    public void actionbarMenuItemClickListener(MenuItem item) {
        super.actionbarMenuItemClickListener(item);
        String title= Objects.requireNonNull(titleEd.getText()).toString();
        String content= Objects.requireNonNull(contentEd.getText()).toString();
        if (item.getItemId() == R.id.menu_yes) {
            try {
                WriteFileJuTemp.write("/Download/HomeMemos/", title, content);
            } catch (Exception e) {
                Toast.makeText(ThisActivity, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        contentEd.clearFocus();
    }
}
