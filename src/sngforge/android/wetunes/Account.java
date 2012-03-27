package sngforge.android.wetunes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Account extends Activity implements OnClickListener {
	
	TextView pinText;
	EditText fname,email;
	Button update;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
		pinText=(TextView)findViewById(R.id.pinText);
		fname=(EditText)findViewById(R.id.chngFname);
		email=(EditText)findViewById(R.id.chngEmail);
		update=(Button)findViewById(R.id.update);
		update.setOnClickListener(this);
		getDetails();
	}

	@Override
	public void onClick(View v) {
		if(v==update)
			updateDetails();
	}
	
	public void updateDetails(){
		ProgressDialog dialog = ProgressDialog.show(this, "WeTunes", "Connecting... Please wait...", true,false);
		try {
			String u = Globals.HOST + "updatedetails.php?mpin="
					+ Globals.settings.getString("pin", "") + "&name="
					+ fname.getText()+"&email="+email.getText();
			URL url = new URL(u.replace(" ", "%20"));
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = rd.readLine();
			if (line.equals("!"))
				Toast.makeText(this, "An error occured! Please check your email address!", Toast.LENGTH_SHORT)
						.show();
			else if (line.equals("?"))
				Toast.makeText(this, "Your account details updated successfully!",
						Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialog.dismiss();
		finish();
	}
	
	public void getDetails(){
		ProgressDialog dialog = ProgressDialog.show(this, "WeTunes", "Connecting... Please wait...", true,false);
		try {
			String u = Globals.HOST + "getaccount.php?mpin="+ Globals.settings.getString("pin", "");
			URL url = new URL(u);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = rd.readLine();
			if(line.equals("!"))
				Toast.makeText(this, "Error occured while trying to fetch account details!", Toast.LENGTH_SHORT).show();
			else{
				String pin=line.toString();
				String thought=rd.readLine();
				Spanned text=Html.fromHtml("Your pin is "+pin+"!<br/>You are thinking: "+thought);
				pinText.setText(text);
				fname.setText(rd.readLine());
				email.setText(rd.readLine());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		dialog.dismiss();
	}
}
