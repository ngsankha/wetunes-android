package sngforge.android.wetunes;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class SuggestionContainer extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Resources res = getResources();
		TabHost tabHost = getTabHost();
		Globals.fTabHost = tabHost;
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, ViewSuggestions.class);
		spec = tabHost.newTabSpec("suggestions")
				.setIndicator("Suggestions", res.getDrawable(R.drawable.tab_suggestion))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, Top.class);
		spec = tabHost.newTabSpec("top")
				.setIndicator("Top Songs", res.getDrawable(R.drawable.tab_top))
				.setContent(intent);
		tabHost.addTab(spec);
	}
}
