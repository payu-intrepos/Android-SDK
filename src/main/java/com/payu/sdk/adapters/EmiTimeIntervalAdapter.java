package com.payu.sdk.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by franklin on 23/7/14.
 */
public class EmiTimeIntervalAdapter extends BaseAdapter {

    Context mContext;
    JSONArray emiArray;

    public EmiTimeIntervalAdapter(Context context, JSONArray emiArray) {
        this.mContext = context;
        this.emiArray = emiArray;
    }

    @Override
    public int getCount() {
        return emiArray.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return emiArray.get(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        JSONObject jsonObject = (JSONObject) getItem(position);
        try {
            ((TextView) view).setText(jsonObject.getString("emiInterval"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
