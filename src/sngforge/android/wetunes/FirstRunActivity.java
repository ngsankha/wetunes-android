package sngforge.android.wetunes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FirstRunActivity extends Activity implements OnClickListener {

	Button register, cancel;
	EditText name, email;
	TextView result;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firstrun);
		register = (Button) findViewById(R.id.register);
		name = (EditText) findViewById(R.id.fulname);
		email = (EditText) findViewById(R.id.email);
		result = (TextView) findViewById(R.id.result);
		cancel = (Button) findViewById(R.id.closeBtn);
		cancel.setOnClickListener(this);
		register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == register)
			register();
		else if (v == cancel)
			finish();
	}

	public void register() {
		register.setEnabled(false);
		register.setText("Please Wait...");
		ProgressDialog dialog = ProgressDialog.show(this, "WeTunes", "Connecting... Please wait...", true,false);
		try {
			String u = Globals.HOST + "createuser.php?name=" + name.getText()
					+ "&email=" + email.getText();
			URL url = new URL(u.replace(" ", "%20"));
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = rd.readLine();
			if (line.equals("!")) {
				result.setText("An error occured please try again!");
				register.setText("Register");
				register.setEnabled(true);
			} else {
				result.setText("Your pin is " + line + " !");
				register.setText("Done!");
				SharedPreferences settings = getSharedPreferences(Globals.PREF,
						0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("pin", line);
				editor.putBoolean("firstRun", false);
				editor.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialog.dismiss();
	}
}
