package com.payu.sdk;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by franklin on 5/3/15.
 */
public class PayUProgressDialog extends ProgressDialog {
    public PayUProgressDialog(Context context, int theme) {
        super(context, theme);
    }
    public PayUProgressDialog(Context context) {
        super(context);
    }
}
