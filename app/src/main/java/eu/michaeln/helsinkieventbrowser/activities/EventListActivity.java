package eu.michaeln.helsinkieventbrowser.activities;

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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.function.Consumer;

import eu.michaeln.helsinkieventbrowser.AutoCompleteTextChangeListener;
import eu.michaeln.helsinkieventbrowser.R;
import eu.michaeln.helsinkieventbrowser.SnackbarVolleyErrorNotifier;
import eu.michaeln.helsinkieventbrowser.adapters.EventListAdapter;
import eu.michaeln.helsinkieventbrowser.api.HelsinkiLinkedEventsApi;
import eu.michaeln.helsinkieventbrowser.entities.AutoCompleteItem;
import eu.michaeln.helsinkieventbrowser.entities.Event;
import eu.michaeln.helsinkieventbrowser.entities.Keyword;
import eu.michaeln.helsinkieventbrowser.entities.Location;
import eu.michaeln.helsinkieventbrowser.entities.PaginatedResult;
import eu.michaeln.helsinkieventbrowser.parcels.CalendarParcel;
import eu.michaeln.helsinkieventbrowser.parcels.EventParcel;

public class EventListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton searchFAB;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView eventsListView;

    private LinearLayout eventFilter;
    private AutoCompleteTextView keywordFilter, placeFilter;
    private Event[] events;

    private HelsinkiLinkedEventsApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        searchFAB = (FloatingActionButton)findViewById(R.id.search_fab);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        eventsListView = (ListView)findViewById(R.id.events_list);

        eventFilter = (LinearLayout)findViewById(R.id.event_filter);
        keywordFilter = (AutoCompleteTextView)findViewById(R.id.keyword);
        placeFilter = (AutoCompleteTextView)findViewById(R.id.place);

        setSupportActionBar(toolbar);

        final Intent callingIntent = getIntent();

        if (callingIntent != null && callingIntent.hasExtra(SearchActivity.INTENT_EXTRA_DATE)) {
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

        new AutoCompleteTextChangeListener(keywordFilter, this) {
            @Override
            protected void textChanged(String text, Consumer<AutoCompleteItem[]> consumer) {
                if (events != null) {
                    ArrayList<AutoCompleteItem> items = new ArrayList<>();

                    for (Event event : events) {
                        for (Keyword keyword : event.getKeywords()) {
                            items.add(new AutoCompleteItem(keyword.getName()));
                        }
                    }

                    AutoCompleteItem[] itemsAsArray = items.toArray(new AutoCompleteItem[items.size()]);
                    consumer.accept(itemsAsArray);
                }

                updateFilteredEventList();
            }
        };

        new AutoCompleteTextChangeListener(placeFilter, this) {

            @Override
            protected void textChanged(String text, Consumer<AutoCompleteItem[]> consumer) {
                if (events != null) {
                    ArrayList<AutoCompleteItem> items = new ArrayList<>();

                    for (Event event : events) {
                        final Location location = event.getLocation();

                        items.add(new AutoCompleteItem(location.getName()));
                    }

                    AutoCompleteItem[] itemsAsArray = items.toArray(new AutoCompleteItem[items.size()]);
                    consumer.accept(itemsAsArray);
                }

                updateFilteredEventList();
            }
        };

        updateEvents();

        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final Event event = events[position];

                if (event != null) {
                    final Intent detailsActivityIntent = new Intent(EventListActivity.this, EventDetailsActivity.class);

                    detailsActivityIntent.putExtra(EventDetailsActivity.INTENT_EXTRA_EVENT, new EventParcel(event));

                    startActivity(detailsActivityIntent);
                }
            }
        });
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

            case R.id.filter_event_list:
                if (eventFilter.getVisibility() == View.VISIBLE) {
                    item.setTitle(R.string.show_event_filters);
                    eventFilter.setVisibility(View.GONE);
                } else {
                    item.setTitle(R.string.hide_event_filters);
                    eventFilter.setVisibility(View.VISIBLE);
                }

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

        final Consumer<PaginatedResult<Event>> eventResultHandler = new Consumer<PaginatedResult<Event>>() {
            @Override
            public void accept(PaginatedResult<Event> eventPaginatedResult) {
                events = eventPaginatedResult.getData();
                updateFilteredEventList();

                swipeRefreshLayout.setRefreshing(false);
            }
        };

        final Intent callingIntent = getIntent();

        if (callingIntent != null && callingIntent.hasExtra(SearchActivity.INTENT_EXTRA_DATE)) {
            final CalendarParcel parcel = callingIntent.getParcelableExtra(SearchActivity.INTENT_EXTRA_DATE);
            final Calendar date = parcel.getCalendar();

            final String keyword = callingIntent.getStringExtra(SearchActivity.INTENT_EXTRA_KEYWORD),
                    place = callingIntent.getStringExtra(SearchActivity.INTENT_EXTRA_PLACE);

            api.searchEvents(date, place, keyword, eventResultHandler);
        } else {
            api.getEvents(eventResultHandler);
        }
    }

    private void updateFilteredEventList() {
        if (events != null) {
            final ArrayList<Event> filteredEvents = new ArrayList<>();

            for (Event event : events) {
                final String keyword = keywordFilter.getText().toString(),
                        place = placeFilter.getText().toString();

                boolean matchesKeyword = keyword.length() == 0;

                for (Keyword eventKeyword : event.getKeywords()) {
                    if (eventKeyword.getName().containsInAnyLanguageWithoutCase(keyword)) {
                        matchesKeyword = true;
                        break;
                    }
                }

                boolean matchesPlace = place.length() == 0 || event.getLocation().getName().containsInAnyLanguageWithoutCase(place);

                if (matchesKeyword && matchesPlace) {
                    filteredEvents.add(event);
                }
            }

            final Event[] filteredEventsAsArray = filteredEvents.toArray(new Event[filteredEvents.size()]);
            final EventListAdapter adapter = new EventListAdapter(EventListActivity.this, filteredEventsAsArray);

            eventsListView.setAdapter(adapter);
        }
    }
}
