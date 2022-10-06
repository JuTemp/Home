package com.JuTemp.Home.Activities;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.JuTemp.Home.FragmentFramework.FragmentLogicJuTemp;
import com.JuTemp.Home.R;
import com.JuTemp.Home.util.FragmentsXmlParserJuTemp;
import com.JuTemp.Home.FragmentFramework.MyFragmentJuTemp;

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
        LayoutInflater layoutInflater = This.getLayoutInflater();
        drawerLayout = This.findViewById(R.id.drawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
        LinearLayoutCompat drawerItemsLinearLayout = This.findViewById(R.id.drawer_items_linearlayout);
        LinearLayoutCompat drawerDeveloperItemsLinearLayout = This.findViewById(R.id.drawer_developer_items_linearlayout);
        View leftDrawer = This.findViewById(R.id.drawer_leftDrawer);
        View rightDrawer = This.findViewById(R.id.drawer_rightDrawer);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                if (drawerView.equals(leftDrawer)) {
                    if (drawerItemsLinearLayout.getChildCount() == 0) {
                        for (int i = 0; i < FRAGMENTLOGIC_ARRAYLIST.size(); i++) {
                            Button bn = (Button) ((LinearLayoutCompat) layoutInflater.inflate(R.layout.drawer_item, drawerItemsLinearLayout)).getChildAt(i);
                            bn.setTag(i + 1);
                            bn.setText(Re.getString(FragmentsXmlParserJuTemp.strId2int(Re, PackageName, (String) Objects.requireNonNull(FragmentsXml.get(i).get("StringAppName")))));
                            bn.setOnClickListener(This);
                        }
                        addViewPreferenceAndExit(layoutInflater, drawerItemsLinearLayout, "Developer", R.string.developer_appname);
                        addViewPreferenceAndExit(layoutInflater, drawerItemsLinearLayout, "Preference", R.string.preference_appname);
                        addViewPreferenceAndExit(layoutInflater, drawerItemsLinearLayout, "Exit", R.string.exit_appname);
                        drawerItemsLinearLayout.getChildAt(drawerItemsLinearLayout.getChildCount() - 1).setBackgroundColor(Color.LTGRAY);
                    }
                } else if (drawerView.equals(rightDrawer)) {
                    if (FRAGMENTLOGIC_DEVELOPER_ARRAYLIST.isEmpty()) {
                        FragmentsDeveloperXml = FragmentsXmlParserJuTemp.initFragmentsXml(This, R.xml.fragments_developer);
                        for (HashMap<String, Object> FragmentXml : FragmentsDeveloperXml)
                            FRAGMENTLOGIC_DEVELOPER_ARRAYLIST.add((String) FragmentXml.get("Id"));
                        if (drawerDeveloperItemsLinearLayout.getChildCount() == 0) {
                            for (int i = 0; i < FRAGMENTLOGIC_DEVELOPER_ARRAYLIST.size(); i++) {
                                Button bn = (Button) ((LinearLayoutCompat) layoutInflater.inflate(R.layout.drawer_item, drawerDeveloperItemsLinearLayout)).getChildAt(i);
                                bn.setTag(-i - 1);
                                bn.setText(Re.getString(FragmentsXmlParserJuTemp.strId2int(Re, PackageName, (String) Objects.requireNonNull(FragmentsDeveloperXml.get(i).get("StringAppName")))));
                                bn.setOnClickListener(This);
                            }
                        }
                    }
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        if (FRAGMENTLOGIC_ARRAYLIST.isEmpty()) { // default
            FragmentsXml = FragmentsXmlParserJuTemp.initFragmentsXml(This, R.xml.fragments);
            for (HashMap<String, Object> FragmentXml : FragmentsXml)
                FRAGMENTLOGIC_ARRAYLIST.add((String) FragmentXml.get("Id"));
        }

        index = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(This).getString(ApplJuTemp.START_ACTIVITY_INDEX, "-1"));
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
        if (indexNew != -2) This.refreshFragment(This, true);
    }

    private final OnClickListener fragment_choose_ocl = view -> {
        ApplJuTemp.fragmentHaveChosen=true;
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
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
            return;
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            Intent intent = new Intent(This, ExitJuTemp.class);
            This.startActivity(intent);
            This.finish();
        } else {
            //ApplJuTemp.hideSoftInput((InputMethodManager) This.getSystemService(Context.INPUT_METHOD_SERVICE), getWindow().getCurrentFocus().getWindowToken());
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        This.getClassFragmentLogic(FRAGMENTLOGIC_ARRAYLIST.get(index)).onActivityResult(This, requestCode, resultCode, intent);
    }

    @Override
    public void onClick(View v) {
        drawerLayout.closeDrawer(GravityCompat.START);
        drawerLayout.closeDrawer(GravityCompat.END);
        Intent intent = null;
        Object vTag = v.getTag();
        if (vTag instanceof String) {
            switch ((String) vTag) {
                case "Developer":
                    drawerLayout.closeDrawer(GravityCompat.START);
                    drawerLayout.openDrawer(GravityCompat.END);
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

    public FragmentLogicJuTemp getClassFragmentLogic(String classname) {
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

