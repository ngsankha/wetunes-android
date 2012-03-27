package sngforge.android.wetunes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class Rate extends Activity implements OnClickListener {
	
	EditText rateTitle,rateArtist,getRateTitle,getRateArtist;
	Button giveRateBtn,getRating;
	RatingBar rating;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rate);
		rateTitle=(EditText)findViewById(R.id.rateTitle);
		rateArtist=(EditText)findViewById(R.id.rateArtist);
		getRateTitle=(EditText)findViewById(R.id.getRateTitle);
		getRateArtist=(EditText)findViewById(R.id.getRateArtist);
		giveRateBtn=(Button)findViewById(R.id.giveRateBtn);
		getRating=(Button)findViewById(R.id.getRating);
		rating=(RatingBar)findViewById(R.id.rating);
		giveRateBtn.setOnClickListener(this);
		getRating.setOnClickListener(this);
		if(Globals.af!=null){
			rateTitle.setText(Globals.af.title);
			rateArtist.setText(Globals.af.artist);
			getRateTitle.setText(Globals.af.title);
			getRateArtist.setText(Globals.af.artist);
		}
	}

	@Override
	public void onClick(View v) {
		if(v==giveRateBtn)
			rate();
		else if(v==getRating)
			getRating();
	}
	
	public void rate(){
		ProgressDialog dialog = ProgressDialog.show(this, "WeTunes", "Connecting... Please wait...", true,false);
		try {
			String u = Globals.HOST + "setrating.php?title=" + rateTitle.getText()+"&artist="+rateArtist.getText()+"&rating="+rating.getRating();
			URL url = new URL(u.replace(" ", "%20"));
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = rd.readLine();
			if (line.equals("!"))
				Toast.makeText(this, "Error occured while rating the song!", Toast.LENGTH_SHORT).show();
			else if(line.equals("?"))
				Toast.makeText(this, "Ratings for song updated successfully!", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialog.dismiss();
	}
	
	public void getRating(){
		ProgressDialog dialog = ProgressDialog.show(this, "WeTunes", "Connecting... Please wait...", true,false);
		try {
			String u = Globals.HOST + "getrating.php?title="+ getRateTitle.getText()+"&artist="+getRateArtist.getText();
			URL url = new URL(u.replace(" ", "%20"));
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = rd.readLine();
			if (line.equals("!"))
				Toast.makeText(
						this,
						"An error occured or may be the song doesn't exist on the database yet!",
						Toast.LENGTH_SHORT).show();
			else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(Html.fromHtml(line))
						.setCancelable(false)
						.setNeutralButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		dialog.dismiss();
	}
}
