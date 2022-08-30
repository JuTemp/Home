package com.JuTemp.Home.MyView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.TextViewCompat;

import com.JuTemp.Home.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseAdapterWuxibusBusStop extends BaseAdapter {

    BaseAdapterWuxibusBusStop This = this;
    Activity ThisActivity = null;
    ArrayList<HashMap<String, Object>> listitems = null;

    public BaseAdapterWuxibusBusStop(Activity ThisActivity, ArrayList<HashMap<String, Object>> listitems) {
        this.ThisActivity = ThisActivity;
        this.listitems = listitems;
    }

    @Override
    public int getCount() {
        return listitems.size()-1;
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
        ViewHolderBusStop holder = null;
        if (convertView == null) {
            convertView = ThisActivity.getLayoutInflater().inflate(R.layout.wuxibus_busstop, null);
            holder = new ViewHolderBusStop();
            holder.stationName = convertView.findViewById(R.id.item_stationname);
            holder.arrival = convertView.findViewById(R.id.item_arrival);
            holder.arrivalBusName = convertView.findViewById(R.id.item_arrival_busname);
            holder.arrivalArrivalTime = convertView.findViewById(R.id.item_arrival_arrivaltime);
            holder.focus = convertView.findViewById(R.id.item_focus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderBusStop) convertView.getTag(); // reuse holder avoiding new ViewHoldDirection(); and findViewById()
        }
        HashMap<String, Object> listitem = listitems.get(position);
        holder.stationName.setText((CharSequence) listitem.get("stationName"));
        if (listitem.containsKey("busName")) {
            holder.arrival.setVisibility(View.VISIBLE);
            holder.arrivalBusName.setText(((CharSequence) listitem.get("busName")));
            holder.arrivalArrivalTime.setText(((CharSequence) listitem.get("arrivalTime")));
        } else {
            holder.arrival.setVisibility(View.GONE);
            holder.arrivalBusName.setText(null);
            holder.arrivalArrivalTime.setText(null);
        }
        return convertView;
    }

    private static class ViewHolderBusStop {
        AppCompatTextView stationName;
        LinearLayoutCompat arrival;
        AppCompatTextView arrivalBusName;
        AppCompatTextView arrivalArrivalTime;
        AppCompatTextView focus;
    }
}
