package sngforge.android.wetunes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SocialActivity extends Activity implements OnClickListener {

	TextView pinInfo;
	EditText thought;
	Button frndBtn, shareBtn,rateBtn,suggestBtn,accntBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social);
		pinInfo = (TextView) findViewById(R.id.pinInfo);
		SharedPreferences settings = getSharedPreferences(Globals.PREF, 0);
		if(settings.getString("pin", "!").equals("!"))
			pinInfo.setText("You have not yet registered on our servers!");
		else
			pinInfo.setText("Your pin is " + settings.getString("pin", "") + " . Share this pin with your friends to connect!");
		frndBtn = (Button) findViewById(R.id.frndsBtn);
		shareBtn = (Button) findViewById(R.id.shareBtn);
		rateBtn = (Button) findViewById(R.id.rateBtn);
		suggestBtn=(Button)findViewById(R.id.suggestBtn);
		thought = (EditText) findViewById(R.id.thought);
		accntBtn=(Button)findViewById(R.id.accntBtn);
		frndBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
		rateBtn.setOnClickListener(this);
		suggestBtn.setOnClickListener(this);
		accntBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == frndBtn) {
			Intent frndsIntent = new Intent(SocialActivity.this, FriendsTab.class);
			SocialActivity.this.startActivity(frndsIntent);
		} else if (v == shareBtn)
			share();
		else if(v==rateBtn){
			Intent intent = new Intent(SocialActivity.this, Rate.class);
			SocialActivity.this.startActivity(intent);
		}else if(v==suggestBtn){
			Intent intent = new Intent(SocialActivity.this, SuggestionContainer.class);
			SocialActivity.this.startActivity(intent);
		}else if(v==accntBtn){
			Intent intent = new Intent(SocialActivity.this, Account.class);
			SocialActivity.this.startActivity(intent);
		}
	}

	public void share() {
		ProgressDialog dialog = ProgressDialog.show(this, "WeTunes", "Connecting... Please wait...", true,false);
		try {
			String u = Globals.HOST + "setthought.php?mpin="
					+ Globals.settings.getString("pin", "") + "&thought="
					+ thought.getText().toString().replace(" ", "%20");
			URL url = new URL(u);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = rd.readLine();
			if (line.equals("!"))
				Toast.makeText(this, "An error occured!", Toast.LENGTH_SHORT)
						.show();
			else if (line.equals("?"))
				Toast.makeText(this, "Your thought updated successfully!",
						Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialog.dismiss();
	}
}
