package com.greemlock.ivApp.Objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.greemlock.ivApp.R;

import java.util.ArrayList;

public class deviceListAdapter extends ArrayAdapter<Device> {

    private static final String TAG = "deviceListAdapter";
    private Context c_activity;
    private int mResource;

    private static class ViewHolder {
        TextView tv_device_name;
        TextView tv_device_address;
        ImageView iv_device_photo;
    }

    public deviceListAdapter(Context context, int resource, ArrayList<Device> objects) {
        super(context, resource, objects);
        c_activity = context;
        mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        String s_device_name = getItem(position).getDevice_name();
        String s_device_address = getItem(position).getDevice_address();

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(c_activity);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.tv_device_name   = convertView.findViewById(R.id.tv_device_name);
            holder.tv_device_address = convertView.findViewById(R.id.tv_device_address);
            holder.iv_device_photo   = convertView.findViewById(R.id.iv_device_photo);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_device_name.setText(s_device_name);
        holder.tv_device_address.setText(s_device_address);
        if (s_device_name == "HC-06"){

            holder.iv_device_photo.setImageResource(R.drawable.ic_baseline_perm_device_information_24);

        }
        else{
            holder.iv_device_photo.setImageResource(R.drawable.ic_baseline_device_unknown_24);
        }

        return convertView;
    }

}
