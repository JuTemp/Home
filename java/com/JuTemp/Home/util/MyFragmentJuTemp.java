package com.JuTemp.Home.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.JuTemp.Home.R;

import java.util.HashMap;
import java.util.Objects;

public class MyFragmentJuTemp extends Fragment {

    Activity ThisActivity = null;
    public FragmentLogicJuTemp This = null;
    HashMap<String, Object> fragmentRe = null;
    Resources Re = null;
    String PackageName = null;

    public MyFragmentJuTemp(@NonNull final Activity ThisActivity, final FragmentLogicJuTemp This, final HashMap<String, Object> fragmentRe) {
        this.ThisActivity = ThisActivity;
        this.This = This;
        this.fragmentRe = fragmentRe;
        Re = ThisActivity.getResources();
        PackageName = ThisActivity.getPackageName();
    }

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        ThisActivity.setTitle(Re.getString(FragmentsXmlParserJuTemp.strId2int(Re, PackageName, (String) Objects.requireNonNull(fragmentRe.get("StringActionbar")))));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(Re.getLayout((int) FragmentsXmlParserJuTemp.strId2int(Re, PackageName, (String) Objects.requireNonNull(fragmentRe.get("Layout")))), container, false);
        This.mainLogic(ThisActivity, This, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        This.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        String strId=(String) Objects.requireNonNull(fragmentRe.get("Actionbar"));
        if (strId.equals("null")) return;
        menuInflater.inflate(FragmentsXmlParserJuTemp.strId2int(Re, PackageName, strId), menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
//        switch (FragmentsXmlParserJuTemp.strId2int(Re, PackageName, (String) Objects.requireNonNull(fragmentRe.get("DrawerId")))) {
        switch ((String) Objects.requireNonNull(fragmentRe.get("DrawerId"))) {
            case "R.id.drawer_webview":
            case "R.id.drawer_lx":
                This.actionbarMenuItemClickListener(item);
                break;

            case "R.id.drawer_urllist":
            case "R.id.drawer_memo":
            case "R.id.drawer_nginx":
                if (item.getItemId() == R.id.menu_import) {
                    This.importJson(ClipJuTemp.getClipboard(ThisActivity));
                }
                if (item.getItemId() == R.id.menu_export) {
                    String str = This.exportJson();
                    ClipJuTemp.setClipboard(ThisActivity, str);
                }
                break;

            default:
                View viewFocusing = ThisActivity.getWindow().getDecorView().findFocus();
                if (!(viewFocusing instanceof TextView)) break;
                TextView tvFocusing = (TextView) viewFocusing;
                if (item.getItemId() == R.id.menu_copy) {
                    ClipJuTemp.setClipboard(ThisActivity, tvFocusing.getText().toString());
                }
                if (item.getItemId() == R.id.menu_paste)
                    tvFocusing.setText(ClipJuTemp.getClipboard(ThisActivity));
                break;
        }
        return true;
    }

}
