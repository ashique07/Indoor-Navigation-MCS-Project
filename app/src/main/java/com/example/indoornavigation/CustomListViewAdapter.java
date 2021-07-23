package com.example.indoornavigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListViewAdapter extends BaseAdapter{

    private ArrayList<TrackLocationModel> items;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListViewAdapter(Context context, ArrayList<TrackLocationModel> items)
    {
        this.items = items;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //      int type = getItemViewType(position);

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.row_layout_1, null);
            holder = new ViewHolder();

            holder.xValue = (TextView) convertView.findViewById(R.id.xValue);
            holder.yValue = (TextView) convertView.findViewById(R.id.yValue);
            holder.zValue = (TextView) convertView.findViewById(R.id.zValue);
            holder.roomNumber = (TextView) convertView.findViewById(R.id.roomNumber);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.xValue.setText(items.get(position).getX());
        holder.yValue.setText(items.get(position).getY());
        holder.zValue.setText(items.get(position).getZ());
        holder.roomNumber.setText(items.get(position).getRoomNumber());
        holder.distance.setText(items.get(position).getDistance());

        return convertView;
    }

    static class ViewHolder {
        TextView xValue;
        TextView yValue;
        TextView zValue;
        TextView roomNumber;
        TextView distance;
    }
}
