package eu.michaeln.helsinkieventbrowser;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import eu.michaeln.helsinkieventbrowser.adapters.DetailsPagerAdapter;

public class EventDetailsActivity extends AppCompatActivity {
    public static final String INTENT_EXTRA_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        final Intent callingIntent = getIntent();

        if (callingIntent.hasExtra(INTENT_EXTRA_ID)) {
            final String id = callingIntent.getStringExtra(INTENT_EXTRA_ID);

            final ViewPager pager = (ViewPager) findViewById(R.id.pager);
            final DetailsPagerAdapter adapter = new DetailsPagerAdapter(getSupportFragmentManager(), id);

            pager.setAdapter(adapter);

            final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Details");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}
