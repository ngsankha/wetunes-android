package sngforge.android.wetunes;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class FriendsTab extends TabActivity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Globals.settings = getSharedPreferences(Globals.PREF, 0);
		Resources res = getResources();
		TabHost tabHost = getTabHost();
		Globals.tabHost = tabHost;
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, Friends.class);
		spec = tabHost.newTabSpec("friends")
				.setIndicator("Friends", res.getDrawable(R.drawable.tab_friends))
				.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, FriendsResolve.class);
		spec = tabHost.newTabSpec("requests")
				.setIndicator("Requests", res.getDrawable(R.drawable.tab_requests))
				.setContent(intent);
		tabHost.addTab(spec);
	}

}
