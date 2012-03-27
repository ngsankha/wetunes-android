package sngforge.android.wetunes;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.widget.TabHost;

public class Globals {

	public static MediaActivity mediaActivity;
	public static MediaPlayer mediaPlayer;
	public static AudioFile af;
	public static TabHost tabHost,fTabHost;
	public static final String PREF = "WeTunesPrefs";
	public static final String HOST = "http://sankha93.net76.net/wetunes/";
	//public static final String HOST = "http://localhost/wetunes_server/";
	public static SharedPreferences settings;
	public static String fpin;
	public static String vidUrl;
}
