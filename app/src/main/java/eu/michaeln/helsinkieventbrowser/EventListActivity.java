package eu.michaeln.helsinkieventbrowser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ListView;

import eu.michaeln.helsinkieventbrowser.api.HelsinkiLinkedEventsApi;

public class EventListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton searchFAB;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView eventsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        searchFAB = (FloatingActionButton)findViewById(R.id.search_fab);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        eventsListView = (ListView)findViewById(R.id.events_list);

        setSupportActionBar(toolbar);

        final int refreshIndicatorColor = ContextCompat.getColor(this, R.color.colorPrimary);
        swipeRefreshLayout.setColorSchemeColors(refreshIndicatorColor);

        final SnackbarVolleyErrorNotifier notifier = new SnackbarVolleyErrorNotifier(swipeRefreshLayout);
        final HelsinkiLinkedEventsApi api = new HelsinkiLinkedEventsApi(getApplicationContext(), notifier);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_list, menu);

        return true;
    }
}
