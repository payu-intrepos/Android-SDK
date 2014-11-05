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
 * Created by franklin on 10/7/14.
 */
public class NetBankingAdapter extends BaseAdapter {

    Context mContext;
    JSONArray availableBanks;

    public NetBankingAdapter(Context context, JSONArray availableBanks) {
        this.mContext = context;
        this.availableBanks = availableBanks;
    }

    @Override
    public int getCount() {
        return availableBanks.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return availableBanks.get(position);
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
            ((TextView) view).setText(jsonObject.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

}
