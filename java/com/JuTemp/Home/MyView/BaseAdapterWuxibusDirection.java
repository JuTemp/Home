package com.JuTemp.Home.MyView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.JuTemp.Home.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseAdapterWuxibusDirection extends BaseAdapter {

    BaseAdapterWuxibusDirection This = this;
    Activity ThisActivity = null;
    ArrayList<HashMap<String, HashMap<String,Object>>> listitems = null;

    public BaseAdapterWuxibusDirection(Activity ThisActivity, ArrayList<HashMap<String, HashMap<String,Object>>> listitems) {
        this.ThisActivity = ThisActivity;
        this.listitems = listitems;
    }

    @Override
    public int getCount() {
        return listitems.size();
    }

    @Override
    public Object getItem(int position) {
        return listitems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderDirection holder = null;
        if (convertView == null) {
            convertView = ThisActivity.getLayoutInflater().inflate(R.layout.wuxibus_direction, null);
            holder = new ViewHolderDirection();
            holder.upBn = convertView.findViewById(R.id.item_direction_up);
            holder.downBn = convertView.findViewById(R.id.item_direction_down);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderDirection) convertView.getTag(); // reuse holder avoiding new ViewHoldDirection(); and findViewById()
        }
        HashMap<String, Object> listitemText = listitems.get(position).get("text");
        holder.upBn.setText((CharSequence) listitemText.get("up"));
        holder.downBn.setText((CharSequence) listitemText.get("down"));
        HashMap<String, Object> listitemTag = listitems.get(position).get("tag");
        holder.upBn.setTag(listitemTag.get("up"));
        holder.downBn.setTag(listitemTag.get("down"));
        return convertView;
    }

    private static class ViewHolderDirection {
        Button upBn;
        Button downBn;
    }
}
