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
public class EmiBankListAdapter extends BaseAdapter {

    Context mContext;
    JSONArray availableEmi;

    public EmiBankListAdapter(Context context, JSONArray availableEmi) {
        this.mContext = context;
        this.availableEmi = availableEmi;
    }

    @Override
    public int getCount() {
        return availableEmi.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return availableEmi.get(i);
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        JSONObject jsonObject = (JSONObject) getItem(i);
        try {
            ((TextView) view).setText(jsonObject.getString("bank"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;

    }
}
