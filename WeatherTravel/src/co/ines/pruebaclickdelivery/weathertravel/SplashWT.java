package co.ines.pruebaclickdelivery.weathertravel;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashWT extends Activity{

	private long splashDelay = 1500;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				Intent intent = new Intent().setClass(SplashWT.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		};
		Timer timer = new Timer();
		timer.schedule(timerTask, splashDelay);
	}
	
}
