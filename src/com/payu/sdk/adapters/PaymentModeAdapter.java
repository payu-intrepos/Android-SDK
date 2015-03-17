package com.payu.sdk.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.payu.sdk.PayU;
import com.payu.sdk.R;

/**
 * Created by franklin.michael on 25-06-2014.
 */
public class PaymentModeAdapter extends BaseAdapter {

    Context mContext;
    PayU.PaymentMode[] mPaymentModes;

    public PaymentModeAdapter(Context context, PayU.PaymentMode[] modes) {
        mContext = context;
        mPaymentModes = modes;

    }

    @Override
    public int getCount() {
        return mPaymentModes.length;
    }

    @Override
    public Object getItem(int position) {
        return mPaymentModes[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.payment_options_list_items, null);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.paymentOptionsTitle);

        if (getItem(position).toString().contentEquals("NB")) {
            txtTitle.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.lock2), null, mContext.getResources().getDrawable(R.drawable.arrow), null);
            txtTitle.setText("Net banking");
        } else if (getItem(position).toString().contentEquals("CC")) {
            txtTitle.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.card), null, mContext.getResources().getDrawable(R.drawable.arrow), null);
            txtTitle.setText("Credit / Debit card");
        } else if (getItem(position).toString().contentEquals("DC")) {
            txtTitle.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.card), null, mContext.getResources().getDrawable(R.drawable.arrow), null);
            txtTitle.setText("Debit card");
        } else if (getItem(position).toString().contentEquals("EMI")) {
            txtTitle.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.coin), null, mContext.getResources().getDrawable(R.drawable.arrow), null);
            txtTitle.setText("EMI");
        } else if (getItem(position).toString().contentEquals("CASH")) {
            txtTitle.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.cash_card), null, mContext.getResources().getDrawable(R.drawable.arrow), null);
            txtTitle.setText("Cash card");
        } else if (getItem(position).toString().contentEquals("STORED_CARDS")) {
            txtTitle.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.store_card), null, mContext.getResources().getDrawable(R.drawable.arrow), null);
            txtTitle.setText("Stored cards");
        } else if (getItem(position).toString().contentEquals("PAYU_MONEY")) {
            txtTitle.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.payu_money), null, mContext.getResources().getDrawable(R.drawable.arrow), null);
            txtTitle.setText("PayU money");
        }/* else {
            txtTitle.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_action_add_person), null, mContext.getResources().getDrawable(R.drawable.arrow), null);
//        txtTitle.setText(PayU.PAYMENT_MODE_TITLES.get(getItem(position)));
            txtTitle.setText(getItem(position).toString());
        }*/

        return convertView;
    }
}
