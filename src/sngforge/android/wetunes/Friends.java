package sngforge.android.wetunes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Friends extends Activity implements OnClickListener,
		OnItemClickListener {

	String pins[];
	Spanned friends[];
	ListView lv;
	Button sendBtn;
	EditText fpin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends);
		lv = (ListView) findViewById(R.id.friendsList);
		sendBtn = (Button) findViewById(R.id.sendBtn);
		fpin = (EditText) findViewById(R.id.fpin);
		sendBtn.setOnClickListener(this);
		lv.setOnItemClickListener(this);
		buildFriendsList();
	}

	public void buildFriendsList() {
		ProgressDialog dialog = ProgressDialog.show(this, "WeTunes", "Connecting... Please wait...", true,false);
		try {
			String u = Globals.HOST + "getfriends.php?pin="
					+ Globals.settings.getString("pin", "");
			URL url = new URL(u);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = rd.readLine();
			if (line.equals("!"))
				Toast.makeText(
						this,
						"An error occured on trying to fetch the friends list!",
						Toast.LENGTH_SHORT).show();
			else {
				StringTokenizer st = new StringTokenizer(line, ",");
				pins = new String[st.countTokens()];
				friends = new Spanned[st.countTokens()];
				for (int i = 0; i < pins.length; i++) {
					pins[i] = st.nextToken();
					u = Globals.HOST + "getfriendupdates.php?pin=" + pins[i];
					url = new URL(u);
					conn = url.openConnection();
					rd = new BufferedReader(new InputStreamReader(
							conn.getInputStream()));
					line = rd.readLine();
					friends[i] = Html.fromHtml(line);
				}
				lv.setAdapter(new ArrayAdapter<Spanned>(this,
						R.layout.friendsitem, R.id.friendsItem, friends));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialog.dismiss();
	}

	@Override
	public void onClick(View v) {
		if (v == sendBtn)
			sendRqst();
	}

	public void sendRqst() {
		ProgressDialog dialog = ProgressDialog.show(this, "WeTunes", "Connecting... Please wait...", true,false);
		try {
			String u = Globals.HOST + "sendrqst.php?fpin="
					+ Globals.settings.getString("pin", "") + "&mpin="
					+ fpin.getText();
			URL url = new URL(u);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = rd.readLine();
			if (line.equals("!"))
				Toast.makeText(this, "An error occured!", Toast.LENGTH_SHORT)
						.show();
			else if (line.equals("?"))
				Toast.makeText(this, "Friend Request sent successfully!",
						Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialog.dismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
		Globals.fpin = pins[i];
		Intent intent = new Intent(Friends.this, FriendProfileContainer.class);
		Friends.this.startActivity(intent);
	}
}
