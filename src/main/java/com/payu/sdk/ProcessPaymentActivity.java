package com.payu.sdk;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.payu.custombrowser.Bank;
import com.payu.custombrowser.PageStatus;
import com.payu.custombrowser.PayUBroadcastInterface;
import com.payu.custombrowser.PayUWebViewClient;

import org.apache.http.util.EncodingUtils;


public class ProcessPaymentActivity extends FragmentActivity implements PageStatus, PayUBroadcastInterface {

    WebView webView;
    ProgressDialog mProgressDialog;
    private BroadcastReceiver mReceiver = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressDialog = new ProgressDialog(this);

        setContentView(R.layout.activity_process_payment);
        webView = (WebView) findViewById(R.id.webview);

        Bank bank = new Bank();
        Bundle args = new Bundle();
        args.putInt("webView", R.id.webview);
        bank.setArguments(args);

        getSupportFragmentManager().beginTransaction().add(R.id.parent, bank).commit();


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.addJavascriptInterface(new PayUJSInterface(), "PayU");
        webView.setWebViewClient(new PayUWebViewClient(bank));


        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressDialog.setMessage(getString(R.string.please_wait));
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                if (newProgress == 100) {
                    mProgressDialog.dismiss();
                }
            }

        });


        webView.postUrl(Constants.PAYMENT_URL, EncodingUtils.getBytes(getIntent().getExtras().getString("postData"), "base64"));

    }

    @Override
    public void onHelpUnavailable() {
        findViewById(R.id.parent).setVisibility(View.GONE);
    }

    @Override
    public void onBankError() {
        findViewById(R.id.parent).setVisibility(View.GONE);
    }

    @Override
    public void onHelpAvailable() {
        findViewById(R.id.parent).setVisibility(View.VISIBLE);
    }

    @Override
    public void registerBroadcast(BroadcastReceiver broadcastReceiver, IntentFilter filter) {
        mReceiver = broadcastReceiver;
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void unregisterBroadcast(BroadcastReceiver broadcastReceiver) {
        if(mReceiver != null){
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private final class PayUJSInterface {
        @JavascriptInterface
        public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(ProcessPaymentActivity.this, "on Success", Toast.LENGTH_LONG).show();
                    if(getCallingActivity() != null) {
                        Log.d("onSuccess", "onSuccess");
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            });

        }

        @JavascriptInterface
        public void onFailure() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(getCallingActivity() != null) {
                        Log.d("onFailure", "onFailure");
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                }
            });
        }
    }


    @Override
    public void onBackPressed(){
        if(getIntent().getExtras().getString(PayU.DISABLE_PAYMENT_PROCESS_BACK_BUTTON) == null)
            super.onBackPressed();
    }

}
