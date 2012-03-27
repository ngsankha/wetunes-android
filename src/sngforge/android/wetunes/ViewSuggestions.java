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

public class ViewSuggestions extends ListActivity {
	
	Spanned list[];
	String songs[];
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		buildSuggestions();
		this.setListAdapter(new ArrayAdapter<Spanned>(this,R.layout.friendsitem, R.id.friendsItem, list));
	}
	
	public void buildSuggestions(){
		ProgressDialog dialog = ProgressDialog.show(this, "WeTunes", "Connecting... Please wait...", true,false);
		try {
			String u = Globals.HOST + "getsuggestions.php?mpin="+ Globals.settings.getString("pin", "");
			URL url = new URL(u);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = rd.readLine();
			if(line.equals("!")){
				list=new Spanned[1];
				list[0]=Html.fromHtml("<i>An error occured!</i>");
			}else if(line.equals("?")){
				list=new Spanned[1];
				list[0]=Html.fromHtml("<i>You don't have any song suggestions!</i>");
			}else{
				StringTokenizer st=new StringTokenizer(line,"|");
				list=new Spanned[(st.countTokens()/2)+1];
				songs=new String[list.length];
				list[0]=Html.fromHtml("<i>You can also send suggestions to your friends by going to Friends and selecting Send Suggestions!</i>");
				for(int i=1;i<list.length;i++){
					String song=st.nextToken();
					String pin=st.nextToken();
					u = Globals.HOST + "getnamebypin.php?pin="+pin;
					url = new URL(u);
					conn = url.openConnection();
					rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String name = rd.readLine();
					String text="<b>"+song+"</b><br/>suggested by "+name+"<br/>";
					songs[i]=song;
					list[i]=Html.fromHtml(text);
					android.util.Log.v("WeTunes",text);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		dialog.dismiss();
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if(position!=0){
			String line=songs[position].replace(" by", "").replace(" ", "+");
			Globals.vidUrl="http://m.youtube.com/results?q="+line;			
			Intent intent = new Intent(ViewSuggestions.this, VideoPlayer.class);
			ViewSuggestions.this.startActivity(intent);
		}
	}
}
