package edu.ces.mobileApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.*;

/* Author: Nate Fox
 * BYU - SAAS Department
 * 
 * see /res/layout/main.xml and /AndroidManifest.xml
 * 
 * main.xml sets the WebView to fill the android screen
 * 
 * AndroidManifest.xml gives permission for app to
 * 	access the Internet.  It also removes the title bar and
 * 	URL bar from the WebView so that the URL cannot be seen
 *  or changed manually.
 *  
 * This java file here creates the WebView, sets the URL, 
 *  takes out zoom controls, allows JavaScript, and makes
 *  sure all links/redirection of any kind will be displayed
 *  in the WebView instead of in the android's browser. 
 */

public class CESMobileActivity extends Activity {

    private WebView mWebView;
    private ProgressDialog pd;
    
    /** Called when the activity is first created. */
	/* This method loads the initial login screen
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        
    	if(!isNetworkAvailable())
        	setContentView(R.layout.networkerror);    	
    	else 
    		loadWebView();
    
    }
    
    private boolean isNetworkAvailable() {
    		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo ni = cm.getActiveNetworkInfo();
    		return ni != null;
    }
    
    /* This method loads the app in the webview
     */
    public void loadWebView() {
        
        //String url = "https://y.byu.edu/ry/ae/prod/ces/cgi/mobileApp.cgi";
    	String url = "https://saasta-dev.byu.edu/auth/dev/fox016/mobile";
        setContentView(R.layout.main);
        mWebView = (WebView)findViewById(R.id.webview);       
        mWebView.setWebViewClient(new FoxWebViewClient()); // Keeps app in web view
        mWebView.getSettings().setJavaScriptEnabled(true); // Allows JavaScript
        mWebView.getSettings().setBuiltInZoomControls(false); // Takes out zoom controls
        mWebView.getSettings().setSupportZoom(false); // Takes out zoom capabilities
        mWebView.loadUrl(url);
        
        pd = ProgressDialog.show(this, "", "Loading .. . Please Wait", true);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
 	    if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
 	        mWebView.goBack();
 	        return true;
 	    }
 	    return super.onKeyDown(keyCode, event);
 	}
    
    /* This class makes sure that any page accessed by a link
     * or a redirection of any kind is loaded through
     * the WebView instead of loaded in the Android's browser
     */    
    private class FoxWebViewClient extends WebViewClient {
    	
 	    @Override
 	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
 	        view.loadUrl(url);
 	        return true;
 	    }
 	    
 	    @Override
 	    public void onPageFinished(WebView view, String url) {
 	    	if(pd.isShowing())
 	    		pd.dismiss();
 	    }
 	    
 	    @Override
 	    public void onPageStarted(WebView view, String url, Bitmap favicon) {
 	    	pd.show();
 	    }
 	}
}