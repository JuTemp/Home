package com.JuTemp.Home.FragmentFramework;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.JuTemp.Home.Activities.ApplJuTemp;
import com.JuTemp.Home.MyView.DragAdapter;
import com.JuTemp.Home.MyView.DragListView;
import com.JuTemp.Home.R;
import com.JuTemp.Home.util.ObjectSerializerJuTemp;
import com.JuTemp.Home.util.UUIDGenerateJuTemp;
import com.alibaba.fastjson2.JSON;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class ListJuTemp extends FragmentLogicJuTemp {

    private String PackageName = null;
    private String ResourcesPrefix = null;
    private String SharedPreferencesName = null;

    protected SharedPreferences sp = null;
    protected SharedPreferences.Editor spe = null;
    protected DragAdapter dragAdapter = null;
    protected HashMap<String, String> spMap = null;
    protected ArrayList<String> spArrayList = null;
    protected List<Map<String, Object>> data = null;
    protected final String SummaryKey = "-";

    public ListJuTemp(final String ResourcesPrefix, final String SharedPreferencesName) {
        this.SharedPreferencesName = SharedPreferencesName;
        this.ResourcesPrefix = ResourcesPrefix;
    }

    protected List<Map<String, Object>> initData() {
        List<Map<String, Object>> listItems = new ArrayList<>();
        for (String key : spArrayList) {
            Map<String, Object> listItem = new HashMap<>();
            final String[] titleAndContent = sp.getString(key, "|").split("\\|", 2);
            listItem.put("key", key);
            listItem.put("title", titleAndContent[0]);
            listItem.put("content", titleAndContent[1]);
            listItems.add(listItem);
        }
        return listItems;
    }

    @Override
    public final void mainLogic(final Activity ThisActivity, final FragmentLogicJuTemp fragmentLogic, final View view) {
        super.mainLogic(ThisActivity, fragmentLogic, view);

        PackageName = ThisActivity.getPackageName();

        sp = ThisActivity.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        spe = sp.edit();
        spMap = (HashMap<String, String>) sp.getAll();
        spArrayList = (ArrayList<String>) ObjectSerializerJuTemp.deserialize(ThisActivity, sp.getString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, new ArrayList<String>())));
        DragListView mainlv = view.findViewById(R.id.listDragListView);
        mainlv.setDragViewId(R.id.list_simple_item_handle);
        data = initData();
        dragAdapter = new DragAdapter(data, ThisActivity, This, R.layout.list_simple_item);
        mainlv.setAdapter(dragAdapter);

        ((MaterialButton) view.findViewById(R.id.listAdd)).setText(strId2int("string", ResourcesPrefix + "_add"));

        Button bnAdd = view.findViewById(R.id.listAdd);
        bnAdd.setOnClickListener(this::bnAddOnClickListener);
        bnAdd.setOnLongClickListener(this::bnAddOnLongClickListener);

        mainlv.setAdapter(dragAdapter);
        mainlv.setOnItemLongClickListener(this::mainlvOnItemLongClickListener);
        mainlv.setOnItemClickListener(this::mainlvOnItemClickListener);
    }

    protected abstract void bnAddOnClickListener(View v);

    protected abstract boolean bnAddOnLongClickListener(View v);

    protected abstract boolean mainlvOnItemLongClickListener(AdapterView<?> parent, View v, int position, long id);

    protected abstract void mainlvOnItemClickListener(AdapterView<?> parent, View v, int position, long id);

    @Override
    public void saveData(List<Map<String, Object>> datasNew) {
        spe.remove(SummaryKey);
        ArrayList<String> spArrayListNew = new ArrayList<>();
        for (Map<String, Object> dataNew : datasNew)
            spArrayListNew.add((String) dataNew.get("key"));
        spe.putString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, spArrayListNew));
        spe.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ApplJuTemp.fragmentHaveChosen && ApplJuTemp.shareFlag) {
            ApplJuTemp.shareFlag = false;
            String keyTest = null;
            do keyTest = UUIDGenerateJuTemp.generateShortUUID();
            while (spArrayList.contains(keyTest));
            final String key = keyTest;
            if (ApplJuTemp.viaShareString.contains("http")) {
                int i = ApplJuTemp.viaShareString.indexOf("http");
                String title = ApplJuTemp.viaShareString.substring(0, i).replace("|", SummaryKey).trim();
                String content = ApplJuTemp.viaShareString.substring(i).trim();
                if (content.contains("#")) content = content.substring(0, content.indexOf("#"));
                spMap.put(key, title + "|" + content);
                spe.putString(key, title + "|" + content);
            } else {
                spMap.put(key, key + "|" + ApplJuTemp.viaShareString);
                spe.putString(key, key + "|" + ApplJuTemp.viaShareString);
            }
            spArrayList.add(key);
            spe.putString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
            spe.commit();
            ((ListJuTemp) This).redraw();
        }
    }

    public void redraw() {
        spMap = (HashMap<String, String>) sp.getAll();
        spArrayList = (ArrayList<String>) ObjectSerializerJuTemp.deserialize(ThisActivity, sp.getString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, new ArrayList<String>())));
        data = initData();
        dragAdapter.notifyDataSetChanged(data);
    }

    @Override
    public void importJson(String str) {
        try {
            Map<String, String> spMapNew = (Map<String, String>) JSON.parse(str);
            ArrayList<String> spArrayListNew = (ArrayList<String>) ObjectSerializerJuTemp.deserialize(ThisActivity, spMapNew.get(SummaryKey));
            for (String keyInNew : spArrayListNew) {
                if (Objects.equals(keyInNew, null)) continue;
                String key = null;
                do key = UUIDGenerateJuTemp.generateShortUUID(); while (spArrayList.contains(key));
                spArrayList.add(key);
                spe.putString(key, spMapNew.get(keyInNew));
            }
            spe.putString(SummaryKey, ObjectSerializerJuTemp.serialize(ThisActivity, spArrayList));
            spe.commit();
            spMap = (HashMap<String, String>) sp.getAll();
            ((ListJuTemp) This).redraw();
        } catch (Exception e) {
            Log.e("import", e.toString());
            Toast.makeText(ThisActivity, Re.getString(R.string.import_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public String exportJson() {
        return new JSONObject(spMap).toString().replace("\\/", "/");
    }

    protected int strId2int(String type, String name) {
        return Re.getIdentifier(name, type, PackageName);
    }
}

