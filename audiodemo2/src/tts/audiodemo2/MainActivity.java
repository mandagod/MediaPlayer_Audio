package tts.audiodemo2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
    public  TextView textView;

    MyPlayer player = null;
    boolean started = false;   //player not started at beginning

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.status);
	}


    public  void playComplete ( View view )
    {
    	Log.v("MainActivity:", "playComplete");
    	showToast( "Play complete!"); 
    	player = null;
    	started = false;
    }
    
    public void onStart(View view) throws InterruptedException {
       if ( !started ){
         player = new MyPlayer ( this, view );
         Thread playerThread = new Thread ( player );
         playerThread.start();   // start player thread
         Toast.makeText(this, "Play started. ",Toast.LENGTH_LONG).show();
         textView.setText("Play started!");
         started = true;
       }
    }
    
    public void pausePlay ( View view )
    {
    	Log.v("pausePlay:", "Play");
        if ( player != null ) {
            if ( player.pausePlayer() )
              textView.setText("Player paused!");
         }
    }
    
    public void resumePlay ( View view )
    {
    	Log.v("resumePlay:", "Play");
        if ( player != null ) {
            if ( player.resumePlayer() )
              textView.setText("Player resumed!");
         }
    }
    
    // stop player
    public void onStop ( View view )
    {
      Log.v("onStop:", "Stop");
      if ( player != null ) {
        if ( player.stopPlayer() )
          textView.setText("Player stopped!");
      }
    }

    // display a Toast message on UI thread
    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(MainActivity.this, toast, 
                		          Toast.LENGTH_SHORT).show();
            }
        });
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
