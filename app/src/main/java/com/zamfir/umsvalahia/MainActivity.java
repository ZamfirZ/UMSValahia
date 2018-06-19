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
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity{

    private FirebaseAnalytics mFirebaseAnalytics;
    private WebView myWebView;
    //......................................
    private AdView mAdView;
    //......................................

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        myWebView = findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        String url = "https://ums.valahia.ro";

        myWebView.loadUrl(url);
        myWebView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myWebView.scrollTo(0, 0);

                    }
                }, 300);

            }
        });


//????????????????????????????????????????????????????????????????????????????????/ FOR SAVE INSTANCE
        this.myWebView.getSettings().setDomStorageEnabled(true);
        this.myWebView.getSettings().setJavaScriptEnabled(true);
//????????????????????????????????????????????????????????????????????????????????/
        myWebView.setHorizontalScrollBarEnabled(false);
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.setOnTouchListener(new WebViewTouchListener());




        //.......................................................................................
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, "ca-app-pub-4878467226249861~8776848389");

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        mAdView = findViewById(R.id.adView);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);



        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

                Toast.makeText(MainActivity.this, "onAdFailedToLoad()", Toast.LENGTH_SHORT).show();

                mAdView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mAdView = findViewById(R.id.adView);

                        // Create an ad request. Check your logcat output for the hashed device ID to
                        // get test ads on a physical device. e.g.
                        AdRequest adRequest = new AdRequest.Builder()
                                .build();

                        // Start loading the ad in the background.
                        mAdView.loadAd(adRequest);
                    }
                },5000);

            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                }
        });


//..................................................................................................................

        if(!isNetworkAvailable(this)) {
            Toast.makeText(this, "No Internet connection", Toast.LENGTH_LONG).show();
            finish();
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
    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}