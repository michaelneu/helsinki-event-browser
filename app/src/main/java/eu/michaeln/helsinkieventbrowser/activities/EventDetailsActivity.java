package eu.michaeln.helsinkieventbrowser.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import eu.michaeln.helsinkieventbrowser.R;
import eu.michaeln.helsinkieventbrowser.adapters.DetailsPagerAdapter;
import eu.michaeln.helsinkieventbrowser.entities.Event;
import eu.michaeln.helsinkieventbrowser.parcels.EventParcel;

public class EventDetailsActivity extends AppCompatActivity {
    public static final String INTENT_EXTRA_EVENT = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        final Intent callingIntent = getIntent();

        if (callingIntent.hasExtra(INTENT_EXTRA_EVENT)) {
            final EventParcel parcel = callingIntent.getParcelableExtra(INTENT_EXTRA_EVENT);
            final Event event = parcel.getEvent();

            final ViewPager pager = (ViewPager) findViewById(R.id.pager);
            final DetailsPagerAdapter adapter = new DetailsPagerAdapter(getSupportFragmentManager(), event);

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
