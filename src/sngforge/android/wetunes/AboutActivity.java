package sngforge.android.wetunes;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class AboutActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String text="<br/><br/><b>WeTunes</b><br/>Version 1.0<br/>Programmed by Sankha Narayan Guria" +
				"<br/>Copyright (C) 2011, Sankha Narayan Guria."+
				"<br/><br/>This is a free program for the Android Operating System, allowing you to socialise with music!<br/><br/>"+
				"For any bug reports and suggestions for new features or improvments, please feel free to contact me!<br/><br/>"+
				"Contact me at sankha93@gmail.com";
		TextView content=new TextView(this);
		content.setText(Html.fromHtml(text));
		setContentView(content);
	}
}
