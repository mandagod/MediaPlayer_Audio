package tts.audiodemo1;

import java.io.IOException;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	MediaPlayer mp = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mp = new MediaPlayer();
		String url = "http://www.forejune.com/android/files/sound1.wav";
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mp.setDataSource(url);
			mp.prepare();    // might take long! (for buffering, etc)
		} catch (IOException e) {
			e.printStackTrace();
		}
		/* catch (IllegalArgumentException | SecurityException
				| IllegalStateException | IOException e) {
			e.printStackTrace();
		}*/
		
		mp.start();

		mp.setOnCompletionListener(new OnCompletionListener() {
		    @Override
		    public void onCompletion(MediaPlayer mp) {
		        onDone();
		    }
		 });
	   if ( mp.isPlaying() )
	     Toast.makeText(this, "Sound playback has started!", 
	    		                         Toast.LENGTH_LONG).show();
	}
	
	protected void onDone()
	{
       Toast.makeText(this, "Sound playback has finished!", Toast.LENGTH_LONG).show();
       mp.release();
       mp = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
