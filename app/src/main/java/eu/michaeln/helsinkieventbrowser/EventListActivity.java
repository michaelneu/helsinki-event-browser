package eu.michaeln.helsinkieventbrowser;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.function.Consumer;

import eu.michaeln.helsinkieventbrowser.adapters.EventListAdapter;
import eu.michaeln.helsinkieventbrowser.api.HelsinkiLinkedEventsApi;
import eu.michaeln.helsinkieventbrowser.entities.Event;
import eu.michaeln.helsinkieventbrowser.entities.PaginatedResult;

public class EventListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton searchFAB;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView eventsListView;

    private HelsinkiLinkedEventsApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        searchFAB = (FloatingActionButton)findViewById(R.id.search_fab);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        eventsListView = (ListView)findViewById(R.id.events_list);

        setSupportActionBar(toolbar);

        final ComponentName callingActivity = getCallingActivity();

        if (callingActivity != null && SearchActivity.class.equals(callingActivity.getClass())) {
            final ActionBar actionBar = getSupportActionBar();

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.search_results);

            searchFAB.setVisibility(View.GONE);
        }

        searchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchActivityIntent = new Intent(EventListActivity.this, SearchActivity.class);
                startActivity(searchActivityIntent);
            }
        });

        final int refreshIndicatorColor = ContextCompat.getColor(this, R.color.colorPrimary);
        swipeRefreshLayout.setColorSchemeColors(refreshIndicatorColor);

        final SnackbarVolleyErrorNotifier notifier = new SnackbarVolleyErrorNotifier(swipeRefreshLayout);
        api = new HelsinkiLinkedEventsApi(getApplicationContext(), notifier);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateEvents();
            }
        });

        updateEvents();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_event_list:
                updateEvents();

                return true;

            case R.id.show_about_activity:
                final Intent aboutActivityIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutActivityIntent);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateEvents() {
        swipeRefreshLayout.setRefreshing(true);

        api.getEvents(new Consumer<PaginatedResult<Event>>() {
            @Override
            public void accept(PaginatedResult<Event> eventPaginatedResult) {
                EventListAdapter adapter = new EventListAdapter(EventListActivity.this, eventPaginatedResult.getData());
                eventsListView.setAdapter(adapter);

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
