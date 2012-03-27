package sngforge.android.wetunes;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class FriendProfileContainer extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Resources res = getResources();
		TabHost tabHost = getTabHost();
		Globals.fTabHost = tabHost;
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, FriendProfile.class);
		spec = tabHost.newTabSpec("friend")
				.setIndicator("Friend's Profile", res.getDrawable(R.drawable.tab_friend))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, SuggestSong.class);
		spec = tabHost.newTabSpec("suggest")
				.setIndicator("Suggest Song", res.getDrawable(R.drawable.tab_suggest))
				.setContent(intent);
		tabHost.addTab(spec);
	}
}
