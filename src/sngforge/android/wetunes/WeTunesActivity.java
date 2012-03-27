package sngforge.android.wetunes;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class WeTunesActivity extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Globals.settings = getSharedPreferences(Globals.PREF, 0);
		Resources res = getResources();
		TabHost tabHost = getTabHost();
		Globals.tabHost = tabHost;
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, MediaActivity.class);
		spec = tabHost.newTabSpec("media")
				.setIndicator("Media", res.getDrawable(R.drawable.tab_player))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, SocialActivity.class);
		spec = tabHost.newTabSpec("social")
				.setIndicator("Social", res.getDrawable(R.drawable.tab_social))
				.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, AboutActivity.class);
		spec = tabHost.newTabSpec("about")
				.setIndicator("About", res.getDrawable(R.drawable.tab_info))
				.setContent(intent);
		tabHost.addTab(spec);

		SharedPreferences settings = getSharedPreferences(Globals.PREF, 0);

		/*
		//this is just to reset the pins in cache
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("pin", "ujojo");
		editor.putBoolean("firstRun", false);
		editor.commit();*/
		

		boolean firstRun = settings.getBoolean("firstRun", true);
		if (firstRun) {
			Intent firstRunIntent = new Intent(WeTunesActivity.this,
					FirstRunActivity.class);
			WeTunesActivity.this.startActivity(firstRunIntent);
		}
	}
}
