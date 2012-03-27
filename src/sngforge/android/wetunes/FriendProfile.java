package sngforge.android.wetunes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FriendProfile extends Activity implements OnClickListener {
	
	TextView msg;
	Button listenBtn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendprofile);
		msg = (TextView)findViewById(R.id.profileData);
		listenBtn=(Button)findViewById(R.id.listenSong);
		listenBtn.setOnClickListener(this);
		ProgressDialog dialog = ProgressDialog.show(this, "WeTunes", "Connecting... Please wait...", true,false);
		try {
			String u = Globals.HOST + "getprofile.php?fpin=" + Globals.fpin;
			URL url = new URL(u);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = rd.readLine();
			if (line.equals("!"))
				line = "Error occured while fetching profile data!";
			msg.setText(Html.fromHtml(line));
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialog.dismiss();
	}
	@Override
	public void onClick(View v) {
		if(v==listenBtn)
			listen();
	}
	
	public void listen(){
		try {
			String u = Globals.HOST + "getplaying.php?pin=" + Globals.fpin;
			URL url = new URL(u);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = rd.readLine();
			if (line.equals("!"))
				Toast.makeText(this,"An error occured!",Toast.LENGTH_SHORT).show();
			else{
				String line1=line.replace(" by", "").replace(" ", "+");
				Globals.vidUrl="http://m.youtube.com/results?q="+line1;
				Intent intent = new Intent(FriendProfile.this, VideoPlayer.class);
				FriendProfile.this.startActivity(intent);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
