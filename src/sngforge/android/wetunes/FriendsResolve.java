package sngforge.android.wetunes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FriendsResolve extends ListActivity {

	String pins[];
	Spanned names[];
	FriendsResolve t;
	boolean exists;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getpFriends();
		t = this;
		this.setTitle("Resolve friend requests");
		this.setListAdapter(new ArrayAdapter<Spanned>(this,
				R.layout.friendsitem, R.id.friendsItem, names));
	}

	public void getpFriends() {
		ProgressDialog dialog = ProgressDialog.show(this, "WeTunes", "Connecting... Please wait...", true,false);
		try {
			String u = Globals.HOST + "getpfriends.php?pin="
					+ Globals.settings.getString("pin", "");
			URL url = new URL(u);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = rd.readLine();
			if (line.equals("!"))
				Toast.makeText(
						this,
						"An error occured on trying to fetch the pending friends list!",
						Toast.LENGTH_SHORT).show();
			else if (line.equals("?")) {
				exists=false;
				names=new Spanned[1];
				names[0]=Html.fromHtml("<i>You don't have any more pending friend requests!</i>");
			} else {
				exists=true;
				StringTokenizer st = new StringTokenizer(line, ",");
				names = new Spanned[st.countTokens()];
				pins = new String[st.countTokens()];
				for (int i = 0; i < pins.length; i++) {
					pins[i] = st.nextToken();
					u = Globals.HOST + "getnamebypin.php?pin=" + pins[i];
					url = new URL(u);
					conn = url.openConnection();
					rd = new BufferedReader(new InputStreamReader(
							conn.getInputStream()));
					line = rd.readLine();
					names[i] = Html
							.fromHtml(line + " ( " + pins[i] + " )<br/>");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialog.dismiss();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int i, long id) {
		super.onListItemClick(l, v, i, id);
		final int j = i;
		if(exists){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Do you want to add " + names[i] + " as your friend?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								ProgressDialog d = ProgressDialog.show(t, "WeTunes", "Connecting... Please wait...", true,false);
								try {
									String u = Globals.HOST
											+ "acceptfriend.php?mpin="
											+ Globals.settings.getString("pin",
													"") + "&fpin=" + pins[j];
									URL url = new URL(u);
									URLConnection conn = url.openConnection();
									BufferedReader rd = new BufferedReader(
											new InputStreamReader(conn
													.getInputStream()));
									String line = rd.readLine();
									if (line.equals("!"))
										Toast.makeText(t, "An error occured!",
												Toast.LENGTH_SHORT).show();
									else
										Toast.makeText(t,
												"Friend successfully added!",
												Toast.LENGTH_SHORT).show();
									getpFriends();
									t.setListAdapter(new ArrayAdapter<Spanned>(
											t, R.layout.friendsitem,
											R.id.friendsItem, names));
								} catch (Exception e) {
									e.printStackTrace();
								}
								d.dismiss();
								dialog.cancel();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						ProgressDialog d = ProgressDialog.show(t, "WeTunes", "Connecting... Please wait...", true,false);
						try {
							String u = Globals.HOST + "rejectfriend.php?mpin="
									+ Globals.settings.getString("pin", "")
									+ "&fpin=" + pins[j];
							URL url = new URL(u);
							URLConnection conn = url.openConnection();
							BufferedReader rd = new BufferedReader(
									new InputStreamReader(conn.getInputStream()));
							String line = rd.readLine();
							if (line.equals("!"))
								Toast.makeText(t, "An error occured!",
										Toast.LENGTH_SHORT).show();
							else
								Toast.makeText(t, "Friend request rejected!",
										Toast.LENGTH_SHORT).show();
							getpFriends();
							t.setListAdapter(new ArrayAdapter<Spanned>(
									t, R.layout.friendsitem, R.id.friendsItem,
									names));
						} catch (Exception e) {
							e.printStackTrace();
						}
						d.dismiss();
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	}
}
