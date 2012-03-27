package sngforge.android.wetunes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Top extends ListActivity {
	
	Spanned songs[];
	String names[];
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getTop();
		this.setListAdapter(new ArrayAdapter<Spanned>(this,
				R.layout.friendsitem, R.id.friendsItem, songs));
	}
	
	public void getTop(){
		ProgressDialog dialog = ProgressDialog.show(this, "WeTunes", "Connecting... Please wait...", true,false);
		try {
			String u = Globals.HOST + "gettop.php";
			URL url = new URL(u);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = rd.readLine();
			if (line.equals("!"))
				Toast.makeText(
						this,
						"An error occured on trying to fetch the top playing songs list!",
						Toast.LENGTH_SHORT).show();
			else {
				StringTokenizer st = new StringTokenizer(line, "|");
				songs = new Spanned[st.countTokens()];
				names=new String[songs.length];
				for (int i = 0; i < songs.length; i++){
					String s=st.nextToken();
					songs[i] = Html.fromHtml((i+1)+". "+s+"<br/>");
					names[i]=s;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialog.dismiss();
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);		
			String line=names[position].replace(" by", "").replace(" ", "+");
			Globals.vidUrl="http://m.youtube.com/results?q="+line;			
			Intent intent = new Intent(Top.this, VideoPlayer.class);
			Top.this.startActivity(intent);
	}
}
