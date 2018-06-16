package com.zamfir.umsvalahia;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView;
    private WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myWebView = (WebView)findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("https://ums.valahia.ro");
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setHorizontalScrollBarEnabled(false);
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.setOnTouchListener(new WebViewTouchListener());
        MobileAds.initialize(this,"ca-app-pub-4878467226249861~2374564902");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-4878467226249861/1496048766");



        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });



        if(!isNetworkAvailable(this)) {
            Toast.makeText(this,"No Internet connection", Toast.LENGTH_LONG).show();
            finish(); //Calling this method to close this activity when internet is not available.
        }
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }
    @Override
    public void onBackPressed() {
        if(myWebView.canGoBack()) {
            myWebView.goBack();
            if(!isNetworkAvailable(this)) {
                Toast.makeText(this,"No Internet connection", Toast.LENGTH_LONG).show();
                finish(); //Calling this method to close this activity when internet is not available.
            }
        } else {
            super.onBackPressed();
            if(!isNetworkAvailable(this)) {
                Toast.makeText(this,"No Internet connection", Toast.LENGTH_LONG).show();
                finish(); //Calling this method to close this activity when internet is not available.
            }
        }
    }
    public class WebViewTouchListener implements View.OnTouchListener {
        private float downX;


        @Override
        public boolean onTouch(final View v, final MotionEvent event) {
            if (event.getPointerCount() > 1) {
                //multi touch
                return true;
            }


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    // set x so that it doesn't move
                    event.setLocation(downX, event.getY());
                    break;
            }
            return false;
        }
    }
}