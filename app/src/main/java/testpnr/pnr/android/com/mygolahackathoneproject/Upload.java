package testpnr.pnr.android.com.mygolahackathoneproject;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by bhargavsarvepalli on 26/07/15.
 */
public class Upload extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_activity);
        WebView myWebView = (WebView) findViewById(R.id.upload_webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("http://192.168.0.163/hack/upload.php");
    }
}
