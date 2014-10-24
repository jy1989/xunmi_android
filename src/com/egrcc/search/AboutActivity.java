package com.egrcc.search;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebViewFragment;


public class AboutActivity extends Activity {

    private static final String ABOUTPAGE = "about.html";
    private static final String ABOUTPAGE_URL = "file:///android_asset/";
    private WebViewFragment aboutWebView;
    private ActionBar actionBar;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        aboutWebView = new WebViewFragment();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                .add(R.id.container, aboutWebView).commit();
	
        }
    }
	
    @Override
    public void onStart() {
        super.onStart();
        aboutWebView.getWebView().loadUrl(ABOUTPAGE_URL + ABOUTPAGE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
		
}
