package com.example.voskart.leaguewebview;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private final static List<String> MYWEBSITES = Arrays.asList("http://fu-tt-league.herokuapp.com", "http://fu-tt-league.herokuapp.com/add", "http://fu-tt-league.herokuapp.com/users", "http://fu-tt-league.herokuapp.com/signup");
    private MWebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pass the websites
        myWebView = new MWebView(this, this.MYWEBSITES);
        // Enabling JavaScript
        myWebView.getSettings().setJavaScriptEnabled(true);
        // Set the frontpage as the site to load
        myWebView.loadUrl(this.MYWEBSITES.get(0));
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        setContentView(myWebView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // TODO: implement if needed (future stuff)
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // TODO: implement if needed (future stuff)
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class MWebView extends WebView {

        Context context;
        GestureDetector gestureDetector;
        List<String> websites;
        int currentSiteIndex;
        MWebView mWebView = this;

        public MWebView(Context context, List<String> websites) {
            super(context);
            this.context = context;
            this.websites = websites;
            this.currentSiteIndex = 0;
            this.gestureDetector = new GestureDetector(context, gestureListener);
        }

        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {

            public boolean onDown(MotionEvent event) {
                return false;
            }

            public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
                if (event1.getRawX() > event2.getRawX()) {
                    // Swipe left, tab forward
                    iterateThroughSites(0);
                    return true;
                } else if (event1.getRawX() < event2.getRawX()){
                    // Swipe right, tab backward
                    iterateThroughSites(1);
                    return true;
                }
                return false;
            }
        };

        private void iterateThroughSites(int forwardFlag){
            // If forward call
            if (forwardFlag == 1){
                // If first site is already reached:
                if (this.currentSiteIndex == 0){
                    // Jump to the last one
                    this.mWebView.loadUrl(this.websites.get(this.websites.size() - 1));
                    this.currentSiteIndex = this.websites.size() - 1;
                } else {
                    // Else iterate through the sites normally
                    this.mWebView.loadUrl(this.websites.get(this.currentSiteIndex - 1));
                    this.currentSiteIndex -= 1;
                }
            // Backward call
            } else {
                // If last site is already reached:
                if (this.currentSiteIndex == this.websites.size() - 1){
                    // Jump to the first one (frontpage)
                    this.mWebView.loadUrl(this.websites.get(0));
                    this.currentSiteIndex = 0;
                } else {
                    // Else iterate through the sites normally
                    this.mWebView.loadUrl(this.websites.get(this.currentSiteIndex + 1));
                    this.currentSiteIndex += 1;
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return gestureDetector.onTouchEvent(event) ||
                    super.onTouchEvent(event);
        }
    }
}
