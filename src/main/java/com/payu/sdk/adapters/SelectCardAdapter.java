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
 * Created by franklin on 1/8/14.
 */
public class SelectCardAdapter extends BaseAdapter {

    Context mContext;
    JSONArray availableCards;

    public SelectCardAdapter(Context context, JSONArray availableCards) {
        this.mContext = context;
        this.availableCards = availableCards;
    }

    @Override
    public int getCount() {
        return availableCards.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return availableCards.get(position);
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
            view = mInflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        }
        JSONObject jsonObject = (JSONObject) getItem(position);
        try {
            ((TextView) view).setText(jsonObject.getString("card_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

}
