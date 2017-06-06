package eu.michaeln.helsinkieventbrowser.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.function.Consumer;

import eu.michaeln.helsinkieventbrowser.AutoCompleteTextChangeListener;
import eu.michaeln.helsinkieventbrowser.R;
import eu.michaeln.helsinkieventbrowser.api.HelsinkiLinkedEventsApi;
import eu.michaeln.helsinkieventbrowser.entities.AutoCompleteItem;
import eu.michaeln.helsinkieventbrowser.parcels.CalendarParcel;

public class SearchActivity extends AppCompatActivity {
    public static final String INTENT_EXTRA_DATE = "date",
            INTENT_EXTRA_KEYWORD= "keyword",
            INTENT_EXTRA_PLACE = "place";

    private Toolbar toolbar;
    private AutoCompleteTextView keyword, place;
    private TextView date;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        keyword = (AutoCompleteTextView)findViewById(R.id.keyword);
        place = (AutoCompleteTextView)findViewById(R.id.place);
        date = (TextView)findViewById(R.id.date);

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.search);

        final HelsinkiLinkedEventsApi api = new HelsinkiLinkedEventsApi(getApplicationContext(), null);

        new AutoCompleteTextChangeListener(keyword, this) {
            @Override
            protected void textChanged(String text, Consumer<AutoCompleteItem[]> consumer) {
                api.autoCompleteKeywords(text, consumer);
            }
        };
        new AutoCompleteTextChangeListener(place, this) {
            @Override
            protected void textChanged(String text, Consumer<AutoCompleteItem[]> consumer) {
                api.autoCompletePlaces(text, consumer);
            }
        };

        calendar = Calendar.getInstance();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int year = calendar.get(Calendar.YEAR),
                        month = calendar.get(Calendar.MONTH),
                        day = calendar.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog dialog = new DatePickerDialog(SearchActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);

                        final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
                        final String formattedDate = formatter.format(calendar.getTime());

                        date.setText(formattedDate);
                    }
                }, year, month, day);

                dialog.show();
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
        getMenuInflater().inflate(R.menu.menu_search, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                final Intent displaySearchedListIntent = new Intent(this, EventListActivity.class);

                displaySearchedListIntent.putExtra(INTENT_EXTRA_DATE, new CalendarParcel(calendar));
                displaySearchedListIntent.putExtra(INTENT_EXTRA_KEYWORD, keyword.getText().toString());
                displaySearchedListIntent.putExtra(INTENT_EXTRA_PLACE, place.getText().toString());

                startActivity(displaySearchedListIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
