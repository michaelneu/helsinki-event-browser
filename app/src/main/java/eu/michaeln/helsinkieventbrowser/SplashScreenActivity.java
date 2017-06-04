package eu.michaeln.helsinkieventbrowser;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Handler delayActivityStartHandler = new Handler();
        delayActivityStartHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent activitySwitchIntent = new Intent(SplashScreenActivity.this, EventListActivity.class);

                startActivity(activitySwitchIntent);
            }
        }, SPLASH_DURATION);
    }
}
