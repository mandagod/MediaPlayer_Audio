package tts.audiodemo2;

import java.io.*;
import java.util.concurrent.locks.*;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.AudioManager;
import android.media.MediaPlayer.OnCompletionListener;

public class MyPlayer  implements Runnable 
{
	MediaPlayer mp = null;	
	MyListener listener = null;
    final Lock mutex = new ReentrantLock();
    final Condition done = mutex.newCondition();
    Intent intent = null;     // not used in this demo
    boolean complete = false;
    MainActivity main;
    View view;
    
    public MyPlayer (){
    	//super ("MyPlayer");
    }
    
    public MyPlayer ( MainActivity main0, View view0 ){
    	main = main0;
    	view = view0;
    	listener = new MyListener();
    }
    
	public MyPlayer ( Intent i ) {
	  //super ("MyPlayer");
	  listener = new MyListener();
	  intent = i;
	}
	
	@Override
	public void run() {
		onHandleIntent ( intent );   
		Log.v("run", "after onHandleIntent");
		main.playComplete( view );
	}
	
	//@Override ( need Override if started as an IntentSerive 
	protected void onHandleIntent(Intent intent) {
		//String url = intent.getStringExtra( URL_ADDRESS );
		mp = new MediaPlayer();
		String url = "http://www.forejune.com/android/files/sound1.wav";
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mp.setDataSource(url);
			mp.prepare();    // might take long! (for buffering, etc)
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*catch (IllegalArgumentException | SecurityException
					| IllegalStateException | IOException e) {
			e.printStackTrace();
		}*/
			
		mp.start();
		
		mp.setOnCompletionListener ( listener );
		try {
			mutex.lock();
			while ( !complete )
	          done.await();           //Condition wait
			mutex.unlock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}    		
	}
	
	// To detect when is the play complete
	class MyListener implements OnCompletionListener
	{
		@Override
		public void onCompletion ( MediaPlayer mp ) {
		   complete = true;
		   mutex.lock();
		   done.signal();    //wake up sleeping thread
		   mutex.unlock();
		   mp.release();     //release resources
		   mp = null;			
		}	
	}
	
	// stop the media player
	public boolean stopPlayer()
	{
	  if( mp !=null && mp.isPlaying() ){
          mp.stop();
          return true;
	  } else
		  return false;
	}
	
	// pause the media player, which can be resumed by calling start()
	public boolean pausePlayer()
	{
		if( mp !=null && mp.isPlaying() ){
            mp.pause();
            return true;
		} else
			return false;
	}
	
	// resume the paused media player
	public boolean resumePlayer()
	{
		if( mp !=null && !mp.isPlaying() ){
            mp.start();
            return true;
		} else
			return false;
	}
}
