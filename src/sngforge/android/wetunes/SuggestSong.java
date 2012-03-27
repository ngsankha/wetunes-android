package sngforge.android.wetunes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SuggestSong extends Activity implements OnClickListener {
	
	EditText title,artist;
	Button suggestBtn;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suggest);
		title=(EditText)findViewById(R.id.title);
		artist=(EditText)findViewById(R.id.artist);
		suggestBtn=(Button)findViewById(R.id.sendSuggestBtn);
		suggestBtn.setOnClickListener(this);
		if(Globals.af!=null){
			title.setText(Globals.af.title);
			artist.setText(Globals.af.artist);
		}
	}

	@Override
	public void onClick(View v) {
		if(v==suggestBtn)
			suggest();
	}
	
	public void suggest(){
		ProgressDialog dialog = ProgressDialog.show(this, "WeTunes", "Connecting... Please wait...", true,false);
		try {
			String u = Globals.HOST + "suggest.php?title="+title.getText()+"&artist="+artist.getText()+"&mpin="+Globals.settings.getString("pin", "")+"&fpin="+Globals.fpin;
			URL url = new URL(u.replace(" ", "%20"));
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = rd.readLine();
			if (line.equals("!"))
				Toast.makeText(this, "An error occured!", Toast.LENGTH_SHORT)
						.show();
			else if (line.equals("?"))
				Toast.makeText(this, "Your suggestion has been sent successfully!",
						Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialog.dismiss();
	}
}
