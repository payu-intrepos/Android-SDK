package com.payu.sdk;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import com.payu.custombrowser.Bank;
import com.payu.custombrowser.PayUWebChromeClient;
import org.apache.http.util.EncodingUtils;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ProcessPaymentActivity extends FragmentActivity {

    private WebView webView;
    private ProgressDialog mProgressDialog;
    private BroadcastReceiver mReceiver = null;
    private ProgressDialog progressDialog;
    private String checkValue;
    private boolean checkProgress;
    private Set<String> set;
    private String webviewUrl;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras()!=null && getIntent().getExtras().containsKey("postData") && !getIntent().getExtras().getString("postData").contains("pg=NB")) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

     /*   InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow((findViewById(android.R.id.content)).getWindowToken(), InputMethodManager.RESULT_HIDDEN);
*/
        mProgressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_process_payment);


        webView = (WebView) findViewById(R.id.webview);

        try {
            Class.forName("com.payu.custombrowser.Bank");

           final Bank bank = new Bank() {
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

                @Override
                public void onHelpUnavailable() {
                    findViewById(R.id.parent).setVisibility(View.GONE);
                    findViewById(R.id.trans_overlay).setVisibility(View.GONE);
                }

                @Override
                public void onBankError() {
                    progressBarVisibility(View.GONE);
                    findViewById(R.id.parent).setVisibility(View.GONE);
                    findViewById(R.id.trans_overlay).setVisibility(View.GONE);
                }

                @Override
                public void onHelpAvailable() {
                    findViewById(R.id.parent).setVisibility(View.VISIBLE);
                }

                @Override
                public void updateSet(Set<String> urlSet,String check){
                            if (urlSet != null && urlSet.size() > 0 && webviewUrl!=null && !urlSet.contains(webviewUrl)) {
                            progressBarVisibility(View.GONE);
                        }

                    set=urlSet;
                    checkValue=check;
                }

                @Override
                public void communicationError(){
                    progressBarVisibility(View.GONE);
                }

            };
            Bundle args = new Bundle();
            args.putInt("webView", R.id.webview);
            args.putInt("tranLayout",R.id.trans_overlay);
            String [] list =  getIntent().getExtras().getString("postData").split("&");
            HashMap<String , String> intentMap = new HashMap<String , String>();
            for (String item : list) {
                String [] list1 =  item.split("=");
                intentMap.put(list1[0], list1[1]);
            }
            if(getIntent().getExtras().containsKey("txnid")) {
                args.putString(Bank.TXN_ID, getIntent().getStringExtra("txnid"));
            } else {
                args.putString(Bank.TXN_ID, intentMap.get("txnid"));
            }
            //args.putString(Bank.TXN_ID, "" + System.currentTimeMillis());
            if(getIntent().getExtras().containsKey("showCustom")) {
                args.putBoolean("showCustom", getIntent().getBooleanExtra("showCustom", false));
            }
            args.putBoolean("showCustom", true);
            bank.setArguments(args);
            findViewById(R.id.parent).bringToFront();
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.face_out).add(R.id.parent, bank).commit();

            webView.setWebChromeClient(new PayUWebChromeClient(bank) {
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);

                    progressBarVisibilityPayuChrome(View.VISIBLE);

                    if(newProgress>=95) {
                        if (checkProgress)
                            progressBarVisibility(View.GONE);
                    }
                }
            });

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }



                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon)
                {
                    super.onPageStarted(view, url, favicon);
                    webviewUrl=url;
                    if(set!=null && set.size()>0 && !set.contains(url)) {
                        checkProgress = true;
                    }

                    //progressBarVisibility(View.GONE);
                   if(checkValue!=null && url.contains(checkValue)) {
                        bank.update();
                    }
                }
            });



        } catch (ClassNotFoundException e) {
            webView.getSettings().setSupportMultipleWindows(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.addJavascriptInterface(new Object() {
                @JavascriptInterface
                public void onSuccess() {
                    onSuccess("");
                }



                @JavascriptInterface
                public void onSuccess(final String result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra("result", result);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
//                }
                    });
                }

                @JavascriptInterface
                public void onFailure() {
                    onFailure("");
                }

                @JavascriptInterface
                public void onFailure(final String result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra("result", result);
                            setResult(RESULT_CANCELED, intent);
                            finish();
                        }
//                }
                    });
                }
            }, "PayU");

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    progressBarVisibility(View.VISIBLE);
                    if (newProgress == 100) {
                        progressBarVisibility(View.GONE);
                    }
                }
            });
            webView.setWebViewClient(new WebViewClient());
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.postUrl(Constants.PAYMENT_URL, EncodingUtils.getBytes(getIntent().getExtras().getString("postData"), "base64"));
    }


    @Override
    public void onBackPressed(){
        boolean disableBack = false;
        try {
            Bundle bundle = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData;
            disableBack = bundle.containsKey("payu_disable_back") && bundle.getBoolean("payu_disable_back");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(!disableBack) {

            Intent intent = new Intent();
            intent.putExtra("result", "");
            setResult(RESULT_CANCELED, intent);
            super.onBackPressed();
        }
    }
    public void progressBarVisibility(int visibility)
    {

        if(visibility==View.GONE || visibility==View.INVISIBLE ) {

            if(progressDialog!=null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        else if (progressDialog==null || !progressDialog.isShowing())
        {
            progressDialog=showProgress(this);
        }
    }

    public void progressBarVisibilityPayuChrome(int visibility)
    {
        if(visibility==View.GONE || visibility==View.INVISIBLE ) {
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
        else if (progressDialog==null)
        {
            progressDialog=showProgress(this);
        }
    }

    public ProgressDialog showProgress(Context context) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        final Drawable[] drawables = {getResources().getDrawable(R.drawable.l_icon1),
                getResources().getDrawable(R.drawable.l_icon2),
                getResources().getDrawable(R.drawable.l_icon3),
                getResources().getDrawable(R.drawable.l_icon4)
        };

        View layout = mInflater.inflate(R.layout.prog_dialog, null);
        final ImageView imageView; imageView = (ImageView) layout.findViewById(R.id.imageView);
        ProgressDialog progDialog = new ProgressDialog(context, R.style.ProgressDialog);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int i = -1;
            @Override
            synchronized public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        i++;
                        if (i >= drawables.length) {
                            i = 0;
                        }
                        imageView.setImageBitmap(null);
                        imageView.destroyDrawingCache();
                        imageView.refreshDrawableState();
                        imageView.setImageDrawable(drawables[i]);
                    }
                });

            }
        }, 0, 500);

        progDialog.show();
        progDialog.setContentView(layout);
        progDialog.setCancelable(true);
        progDialog.setCanceledOnTouchOutside(false);
        return progDialog;
    }




}