package com.JuTemp.Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.JuTemp.Home.util.FragmentLogicJuTemp;
import com.JuTemp.Home.util.FragmentsXmlParserJuTemp;
import com.JuTemp.Home.util.MyFragmentJuTemp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class HomeJuTemp extends AppCompatActivity implements OnClickListener {
    final HomeJuTemp This = this;
    String PackageName = null;

    static ArrayList<HashMap<String, Object>> FragmentsXml = new ArrayList<>();
    static ArrayList<String> FRAGMENTLOGIC_ARRAYLIST = new ArrayList<>();
    static ArrayList<HashMap<String, Object>> FragmentsDeveloperXml = new ArrayList<>();
    static ArrayList<String> FRAGMENTLOGIC_DEVELOPER_ARRAYLIST = new ArrayList<>();

    DrawerLayout drawerLayout = null;
    Resources Re = null;

    AlertDialog fragment_choose_alertdialog = null;

    static int index = 0;
    FragmentLogicJuTemp FragmentLogicIndex = null;
    static boolean flagOnResume = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
//        This.findViewById(R.id.drawer).getRootView().setBackground(This.getDrawable(R.drawable.home_bg));

        PackageName = This.getPackageName();
        Re = This.getResources();
        drawerLayout = This.findViewById(R.id.drawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);

        if (FRAGMENTLOGIC_ARRAYLIST.isEmpty()) {
            FragmentsXml = FragmentsXmlParserJuTemp.initFragmentsXml(This, false);
            for (HashMap<String, Object> FragmentXml : FragmentsXml)
                FRAGMENTLOGIC_ARRAYLIST.add((String) FragmentXml.get("Id"));
        }
        if (FRAGMENTLOGIC_DEVELOPER_ARRAYLIST.isEmpty()) {
            FragmentsDeveloperXml = FragmentsXmlParserJuTemp.initFragmentsXml(This, true);
            for (HashMap<String, Object> FragmentXml : FragmentsDeveloperXml)
                FRAGMENTLOGIC_DEVELOPER_ARRAYLIST.add((String) FragmentXml.get("Id"));
        }

        LayoutInflater layoutInflater = This.getLayoutInflater();
        LinearLayoutCompat drawerItemsLinearLayout = This.findViewById(R.id.drawer_items_linearlayout);
        for (int i = 0; i < FRAGMENTLOGIC_ARRAYLIST.size(); i++) {
            Button bn = (Button) ((LinearLayoutCompat) layoutInflater.inflate(R.layout.drawer_item, drawerItemsLinearLayout)).getChildAt(i);
            bn.setTag(i + 1);
            bn.setText(Re.getString(FragmentsXmlParserJuTemp.strId2int(Re, PackageName, (String) Objects.requireNonNull(FragmentsXml.get(i).get("StringAppName")))));
            bn.setOnClickListener(This);
        }
        for (int i = 0; i < FRAGMENTLOGIC_DEVELOPER_ARRAYLIST.size(); i++) {
            Button bn = (Button) ((LinearLayoutCompat) layoutInflater.inflate(R.layout.drawer_item, This.findViewById(R.id.drawer_developer_items_linearlayout))).getChildAt(i);
            bn.setTag(-i - 1);
            bn.setText(Re.getString(FragmentsXmlParserJuTemp.strId2int(Re, PackageName, (String) Objects.requireNonNull(FragmentsDeveloperXml.get(i).get("StringAppName")))));
            bn.setOnClickListener(This);
        }
        addViewPreferenceAndExit(layoutInflater, drawerItemsLinearLayout, "Developer", R.string.developer_appname);
        addViewPreferenceAndExit(layoutInflater, drawerItemsLinearLayout, "Preference", R.string.preference_appname);
        addViewPreferenceAndExit(layoutInflater, drawerItemsLinearLayout, "Exit", R.string.exit_appname);
        drawerItemsLinearLayout.getChildAt(drawerItemsLinearLayout.getChildCount() - 1).setBackgroundColor(Color.LTGRAY);

        index = PreferenceManager.getDefaultSharedPreferences(This).getInt(ApplJuTemp.START_ACTIVITY_INDEX, -1);
        if (index >= 0) This.refreshFragment(This, false);

        flagOnResume = true;
    }

    private void addViewPreferenceAndExit(LayoutInflater layoutInflater, LinearLayoutCompat drawerItemsLinearLayout, String tagString, int textId) {
        Button bn = (Button) ((LinearLayoutCompat) layoutInflater.inflate(R.layout.drawer_item, drawerItemsLinearLayout)).getChildAt(drawerItemsLinearLayout.getChildCount() - 1);
        bn.setTag(tagString);
        bn.setText(Re.getString(textId));
        bn.setOnClickListener(This);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        ((FragmentLogicJuTemp)((MyFragmentJuTemp)This.getFragmentManager().findFragmentById(R.id.homeFrameLayout)).This).requestFocus(This,EditText);
        if (Objects.equals(This.getIntent().getAction(), Intent.ACTION_SEND)) {
            if (!flagOnResume) return;
            ApplJuTemp.shareFlag = true;
            ApplJuTemp.viaShareString = This.getIntent().getClipData().getItemAt(0).getText().toString();
            View alertdialogView = This.getLayoutInflater().inflate(R.layout.fragment_choose_view, null);
            fragment_choose_alertdialog = new AlertDialog.Builder(This)
                    .setTitle(This.getResources().getString(R.string.fragment_choose_alertdialog_title))
                    .setView(alertdialogView)
                    .setCancelable(false)
                    .create();
            fragment_choose_alertdialog.show();
            for (int i : new int[]{R.id.fragment_choose_webview, R.id.fragment_choose_urllist, R.id.fragment_choose_qr}) {
                alertdialogView.findViewById(i).setOnClickListener(fragment_choose_ocl);
            }
            flagOnResume = false;
            return;
        }

        // OverlayWindowService intent to OverlayWindowJuTemp
        int indexNew = This.getIntent().getIntExtra("index", -2);
        if (indexNew != -2) {
            This.refreshFragment(This, true);
            return;
        }
    }

    private final OnClickListener fragment_choose_ocl = view -> {
        switch (view.getId()) {
            case R.id.fragment_choose_webview:
                index = FRAGMENTLOGIC_ARRAYLIST.indexOf("WebViewJuTemp");
                break;
            case R.id.fragment_choose_urllist:
                index = FRAGMENTLOGIC_ARRAYLIST.indexOf("URLListJuTemp");
                break;
            case R.id.fragment_choose_qr:
                index = FRAGMENTLOGIC_ARRAYLIST.indexOf("QrJuTemp");
                break;
            default:
                break;
        }
        This.refreshFragment(This, false);
        fragment_choose_alertdialog.dismiss();
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
            return;
        }
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            Intent intent = new Intent(This, ExitJuTemp.class);
            This.startActivity(intent);
            This.finish();
        } else {
            //ApplJuTemp.hideSoftInput((InputMethodManager) This.getSystemService(Context.INPUT_METHOD_SERVICE), getWindow().getCurrentFocus().getWindowToken());
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        This.getClassFragmentLogic(FRAGMENTLOGIC_ARRAYLIST.get(index)).onActivityResult(This, requestCode, resultCode, intent);
    }

    @Override
    public void onClick(View v) {
        drawerLayout.closeDrawer(Gravity.LEFT);
        drawerLayout.closeDrawer(Gravity.RIGHT);
        Intent intent = null;
        Object vTag = v.getTag();
        if (vTag instanceof String) {
            switch ((String) vTag) {
                case "Developer":
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    drawerLayout.openDrawer(Gravity.RIGHT);
                    break;
                case "Preference":
                    intent = new Intent(This, PreferenceJuTemp.class);
                    This.startActivity(intent);
                    break;
                case "Exit":
                    intent = new Intent(This, ExitJuTemp.class);
                    This.startActivity(intent);
                    This.finish();
                    break;
                default:
                    Toast.makeText(This, "Not completely deal with DrawerTag<0", Toast.LENGTH_LONG).show();
                    break;
            }
            return;
        }
        index = (((int) vTag) > 0 ? ((int) vTag) : -((int) vTag)) - 1;
        This.refreshFragment(This, ((int) vTag) < 0);
    }

    private FragmentLogicJuTemp getClassFragmentLogic(String classname) {
        return (FragmentLogicJuTemp) This.getClassHome("FragmentLogics." + classname);
    }

    private Object getClassHome(String classname) {
        try {
            return Class.forName(PackageName + "." + classname).newInstance();
        } catch (Exception ex) {
            //Log.e("Home:getClassHome", ex.toString());
            ex.printStackTrace();
        }
        return null;
    }

    private void refreshFragment(final Activity ThisActivity, boolean whetherDeveloper) {
        if (ThisActivity.getFragmentManager().findFragmentById(R.id.homeFrameLayout) != null)
            ThisActivity.getFragmentManager().beginTransaction().remove(ThisActivity.getFragmentManager().findFragmentById(R.id.homeFrameLayout));
        ThisActivity.getFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,
                new MyFragmentJuTemp(ThisActivity, whetherDeveloper ? This.getClassFragmentLogic(FRAGMENTLOGIC_DEVELOPER_ARRAYLIST.get(index)) : This.getClassFragmentLogic(FRAGMENTLOGIC_ARRAYLIST.get(index)),
                        (whetherDeveloper ? FragmentsDeveloperXml : FragmentsXml).get(index))).commit();
    }


}

