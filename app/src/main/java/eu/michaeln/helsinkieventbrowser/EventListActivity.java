package eu.michaeln.helsinkieventbrowser;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ListView;

public class EventListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton searchFAB;
    private ListView eventsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        searchFAB = (FloatingActionButton)findViewById(R.id.search_fab);
        eventsListView = (ListView)findViewById(R.id.events_list);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_list, menu);

        return true;
    }
}
