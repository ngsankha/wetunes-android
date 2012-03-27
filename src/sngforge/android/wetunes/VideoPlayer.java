package sngforge.android.wetunes;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class VideoPlayer extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView web=new WebView(this);
        setContentView(web);
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(Globals.vidUrl);
	}
}
