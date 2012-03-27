package sngforge.android.wetunes;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MediaActivity extends Activity implements OnClickListener,
		OnSeekBarChangeListener {

	ImageButton selectFileBtn, playBtn, stopBtn;
	Button tellFriends;
	SeekBar volumeBar;
	TextView title, album, artist;
	boolean first,stopped;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Globals.mediaActivity = this;
		setContentView(R.layout.playerlayout);
		first = true;
		stopped=true;
		selectFileBtn = (ImageButton) findViewById(R.id.selectFileBtn);
		selectFileBtn.setOnClickListener(this);
		stopBtn = (ImageButton) findViewById(R.id.stopBtn);
		stopBtn.setOnClickListener(this);
		stopBtn.setEnabled(false);
		playBtn = (ImageButton) findViewById(R.id.playBtn);
		playBtn.setOnClickListener(this);
		playBtn.setEnabled(false);
		title = (TextView) findViewById(R.id.title);
		artist = (TextView) findViewById(R.id.artist);
		album = (TextView) findViewById(R.id.album);
		volumeBar = (SeekBar) findViewById(R.id.volumeBar);
		volumeBar.setOnSeekBarChangeListener(this);
		tellFriends = (Button) findViewById(R.id.tellFriends);
		tellFriends.setOnClickListener(this);
		title.setText("");
		album.setText("");
		artist.setText("");
	}

	@Override
	public void onClick(View v) {
		if (v == selectFileBtn) {
			Intent intent = new Intent(MediaActivity.this, FileChooser.class);
			MediaActivity.this.startActivity(intent);
		} else if (v == playBtn)
			pausePlay();
		else if (v == stopBtn)
			stop();
		else if (v == tellFriends)
			tellFriends();
	}

	public void fileSelected(String f) {
		if (!first) {
			Globals.mediaPlayer.stop();
			Globals.mediaPlayer.release();
			Globals.mediaPlayer = null;
		}
		Globals.mediaPlayer = new MediaPlayer();
		try {
			Globals.mediaPlayer.setDataSource(f);
			Globals.mediaPlayer.prepare();
			Globals.mediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		first = false;
		playBtn.setImageResource(R.drawable.pause);
		File file = new File(f);
		try {
			populateTags(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		title.setText(Globals.af.title);
		artist.setText(Globals.af.artist);
		album.setText(Globals.af.album);
		playBtn.setEnabled(true);
		stopBtn.setEnabled(true);
		Toast.makeText(this, file.getName() + " is playing!",
				Toast.LENGTH_SHORT).show();
		Globals.mediaPlayer.start();
		updateOnServer();
		stopped=false;
	}
	
	public void updateOnServer(){
		try {
			String u = Globals.HOST + "setplaying.php?mpin="
					+ Globals.settings.getString("pin", "") + "&title="
					+ Globals.af.title.replace(" ", "%20")+"&artist="+Globals.af.artist.replace(" ", "%20");
			URL url = new URL(u);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = rd.readLine();
			if (line.equals("!"))
				Log.v("weTunes", "Now Playing failed to be updated on server!");
			else if (line.equals("?"))
				Log.v("weTunes", "Now Playing updated on server!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pausePlay() {
		if (Globals.mediaPlayer.isPlaying()) {
			Globals.mediaPlayer.pause();
			playBtn.setImageResource(R.drawable.play);
		} else {
			if(stopped){
				updateOnServer();
				stopped=false;
			}
			Globals.mediaPlayer.start();
			playBtn.setImageResource(R.drawable.pause);
		}
	}

	public void stop() {
		Globals.mediaPlayer.pause();
		Globals.mediaPlayer.seekTo(0);
		stopped=true;
		playBtn.setImageResource(R.drawable.play);
	}

	public void populateTags(File f) throws Exception {
		Globals.af = new AudioFile();
		MusicMetadataSet metaset = new MyID3().read(f);
		MusicMetadata meta = (MusicMetadata) metaset.getSimplified();
		Globals.af.title = meta.getSongTitle();
		Globals.af.album = meta.getAlbum();
		Globals.af.artist = meta.getArtist();
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int vol, boolean arg2) {
		if(Globals.mediaPlayer!=null)
			Globals.mediaPlayer.setVolume(vol / 100.0f, vol / 100.0f);
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
	}

	public void tellFriends() {
		Intent intent = new Intent(MediaActivity.this, TellFriends.class);
		MediaActivity.this.startActivity(intent);
	}
}
