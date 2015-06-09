package com.payu.sdk.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.payu.sdk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by franklin on 10/7/14.
 */
public class StoredCardAdapter extends BaseAdapter {

    Context mContext;
    JSONArray mStoredCards;

    public StoredCardAdapter(Context context, JSONArray storedCards) {
        this.mContext = context;
        this.mStoredCards = storedCards;
    }

    @Override
    public int getCount() {
        return mStoredCards.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return mStoredCards.get(i);
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
//            view = mInflater.inflate(R.layout.stored_card_details_list_items, null);
            view = mInflater.inflate(R.layout.store_card_item, null);
        }

        JSONObject jsonObject = (JSONObject) getItem(i);

        // set text here
        try {
            ((TextView) view.findViewById(R.id.cardLabelTextView)).setText(jsonObject.getString("card_name"));
            ((TextView) view.findViewById(R.id.cardNumberTextView)).setText(jsonObject.getString("card_no"));
            ((TextView) view.findViewById(R.id.cardNumberTextView)).setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.arrow), null);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;

    }
}
