package com.JuTemp.Home.FragmentLogics;

import android.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.JuTemp.Home.Activities.ApplJuTemp;
import com.JuTemp.Home.Activities.HomeJuTemp;
import com.JuTemp.Home.FragmentFramework.ListJuTemp;
import com.JuTemp.Home.FragmentFramework.MyFragmentJuTemp;
import com.JuTemp.Home.R;
import com.JuTemp.Home.util.FragmentsXmlParserJuTemp;
import com.JuTemp.Home.util.ObjectSerializerJuTemp;
import com.JuTemp.Home.util.UUIDGenerateJuTemp;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoJuTemp extends ListJuTemp {

    private String ResourcesPrefix=null;

    public MemoJuTemp() {
        super("memo", ApplJuTemp.MEMO);
        ResourcesPrefix="memo";
    }

    @Override
    protected void bnAddOnClickListener(View v) {
        final View adView = ThisActivity.getLayoutInflater().inflate(strId2int("layout", ResourcesPrefix + "_alertdialog_view"), null);
        String keyTest = null;
        do keyTest = UUIDGenerateJuTemp.generateShortUUID();
        while (spArrayList.contains(keyTest));
        final String key = keyTest;
        new AlertDialog.Builder(ThisActivity)
                .setTitle(Re.getString(strId2int("string", ResourcesPrefix + "_dialog_textin_title")))
                .setView(adView)
                .setPositiveButton(Re.getString(strId2int("string", ResourcesPrefix + "_dialog_confirm")), (dialogInterface, which) -> {
                    String title = ((EditText) adView.findViewById(strId2int("id", ResourcesPrefix + "_title"))).getText().toString();
                    if (title.contains("|")) {
                        Toast.makeText(ThisActivity, Re.getString(strId2int("string", ResourcesPrefix + "_containsplit_error")), Toast.LENGTH_LONG).show();
                        return;
                    }
                    String content = ((EditText) adView.findViewById(strId2int("id", ResourcesPrefix + "_content"))).getText().toString();
                    spMap.put(key, title + "|" + content);
                    spArrayList.add(spArrayList.size(), key);
                    spe.putString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
                    spe.putString(key, title + "|" + content);
                    spe.commit();
                    ((ListJuTemp) This).redraw();
                })
                .setNegativeButton(Re.getString(strId2int("string", ResourcesPrefix + "_dialog_cancel")), (dialogInterface, which) -> {
                    spMap.put(key, "|");
                    spArrayList.add(key);
                    spe.putString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
                    spe.putString(key, "|");
                    spe.commit();
                    ((ListJuTemp) This).redraw();
                })
                .setNeutralButton(Re.getString(strId2int("string", ResourcesPrefix + "_dialog_delete")), null)
                .create().show();
    }

    @Override
    protected boolean bnAddOnLongClickListener(View v) {
        new AlertDialog.Builder(ThisActivity)
                .setTitle(Re.getString(strId2int("string", ResourcesPrefix + "_dialog_deleteall_title")))
                .setPositiveButton(Re.getString(strId2int("string", ResourcesPrefix + "_dialog_delete")), (dialogInterface, which) -> {
                    spMap.clear();
                    spArrayList.clear();
                    spe.clear();
                    spe.putString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, new ArrayList<String>()));
                    spe.commit();
                    spMap = (HashMap<String, String>) sp.getAll();
                    ((ListJuTemp) This).redraw();
                })
                .setNegativeButton(Re.getString(strId2int("string", ResourcesPrefix + "_dialog_cancel")), null)
                .create().show();
        return true;
    }

    @Override
    protected boolean mainlvOnItemLongClickListener(AdapterView<?> parent, View v, int position, long id) {
        final View adView = ThisActivity.getLayoutInflater()
                .inflate(strId2int("layout", ResourcesPrefix + "_alertdialog_view"), null);
        ((EditText) adView.findViewById(strId2int("id", ResourcesPrefix + "_title"))).setText((String) data.get(position).get("title"));
        ((EditText) adView.findViewById(strId2int("id", ResourcesPrefix + "_content"))).setText((String) data.get(position).get("content"));
        new AlertDialog.Builder(ThisActivity)
                .setTitle(Re.getString(strId2int("string", ResourcesPrefix + "_dialog_modify_title")))
                .setView(adView)
                .setPositiveButton(Re.getString(strId2int("string", ResourcesPrefix + "_dialog_modify")), (dialogInterface, which) -> {
                    String title = ((EditText) adView.findViewById(strId2int("id", ResourcesPrefix + "_title"))).getText().toString();
                    if (title.contains("|")) title = title.replace("|", SummaryKey);
                    String content = ((EditText) adView.findViewById(strId2int("id", ResourcesPrefix + "_content"))).getText().toString();
                    String key = spArrayList.get(position);
                    spMap.put(key, title + "|" + content);
                    spe.putString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
                    spe.putString(key, title + "|" + content);
                    spe.commit();
                    ((ListJuTemp) This).redraw();
                })
                .setNegativeButton(Re.getString(strId2int("string", ResourcesPrefix + "_dialog_cancel")), null)
                .setNeutralButton(Re.getString(strId2int("string", ResourcesPrefix + "_dialog_delete")), (dialogInterface, which) -> new AlertDialog.Builder(ThisActivity)
                        .setTitle(Re.getString(strId2int("string", ResourcesPrefix + "_dialog_delete_title")))
                        .setPositiveButton(Re.getString(strId2int("string", ResourcesPrefix + "_dialog_delete")), (dialogInterface1, which1) -> {
                            spMap.remove(spArrayList.get(position));
                            spe.remove(spArrayList.get(position));
                            spArrayList.remove(position);
                            spe.putString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
                            spe.commit();
                            spMap = (HashMap<String, String>) sp.getAll();
                            //mainlv.removeView(view);
                            ((ListJuTemp) This).redraw();
                        })
                        .setNegativeButton(Re.getString(strId2int("string", ResourcesPrefix + "_dialog_cancel")), null)
                        .create().show())
                .create().show();
        return true;
    }

    @Override
    protected void mainlvOnItemClickListener(AdapterView<?> parent, View v, int position, long id) {
        ArrayList<HashMap<String, Object>> FragmentsOmitXml = FragmentsXmlParserJuTemp.initFragmentsXml(ThisActivity, R.xml.fragments_omit);
         ArrayList<String> FRAGMENTLOGIC_OMIT_ARRAYLIST = new ArrayList<>();
        for (HashMap<String, Object> FragmentXml : FragmentsOmitXml)
            FRAGMENTLOGIC_OMIT_ARRAYLIST.add((String) FragmentXml.get("Id"));
        if (ThisActivity.getFragmentManager().findFragmentById(R.id.homeFrameLayout) != null)
            ThisActivity.getFragmentManager().beginTransaction().remove(ThisActivity.getFragmentManager().findFragmentById(R.id.homeFrameLayout));
        ThisActivity.getFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,
                new MyFragmentJuTemp(ThisActivity, ((HomeJuTemp) ThisActivity).getClassFragmentLogic(FRAGMENTLOGIC_OMIT_ARRAYLIST.get(0)), FragmentsOmitXml.get(0))).commit();
    }
}