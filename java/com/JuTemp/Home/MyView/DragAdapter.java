package com.JuTemp.Home.MyView;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.JuTemp.Home.R;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DragAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private Context context;

    public DragAdapter(List<Map<String, Object>> data, Context context) {
        this.data = data;
        this.context = context;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(context, R.layout.urllist_simple_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.si_title);
            holder.content = (TextView) convertView.findViewById(R.id.si_content);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.title.setText(Objects.requireNonNull(data.get(position).get("title")).toString());
        holder.content.setText(Objects.requireNonNull(data.get(position).get("content")).toString());

        return convertView;
    }

    public void notifyDataSetChanged(List<Map<String, Object>> data) {
        this.data=data;
        super.notifyDataSetChanged();
    }

    public void change(int start, int end) {
        Map<String, Object> srcData = data.get(start);
        data.remove(srcData);
        data.add(end, srcData);
        notifyDataSetChanged();
    }

    private class Holder {
        TextView title;
        TextView content;
    }
}