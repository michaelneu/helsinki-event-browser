package eu.michaeln.helsinkieventbrowser.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import eu.michaeln.helsinkieventbrowser.R;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 3000;

    private boolean switchedActivity = false;

    private final Runnable continueRunnable = new Runnable() {
        @Override
        public void run() {
            if (!switchedActivity) {
                switchedActivity = true;
                final Intent activitySwitchIntent = new Intent(SplashScreenActivity.this, EventListActivity.class);

                startActivity(activitySwitchIntent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final ConstraintLayout splashWrapper = (ConstraintLayout)findViewById(R.id.splash_wrapper);
        splashWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueRunnable.run();
            }
        });

        final Handler delayActivityStartHandler = new Handler();

        delayActivityStartHandler.postDelayed(continueRunnable, SPLASH_DURATION);
    }
}
