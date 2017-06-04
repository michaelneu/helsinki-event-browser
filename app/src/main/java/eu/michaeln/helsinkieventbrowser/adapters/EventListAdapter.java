package eu.michaeln.helsinkieventbrowser.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import eu.michaeln.helsinkieventbrowser.R;
import eu.michaeln.helsinkieventbrowser.entities.Event;

public final class EventListAdapter extends ArrayAdapter<Event> {
    private final SimpleDateFormat dateFormatter;

    public EventListAdapter(@NonNull Context context, Event[] events) {
        super(context, R.layout.list_item_event, events);

        dateFormatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.ENGLISH);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_event, null);
        }

        final Event event = getItem(position);

        final TextView title = (TextView)convertView.findViewById(R.id.title),
                location = (TextView)convertView.findViewById(R.id.location),
                date = (TextView)convertView.findViewById(R.id.date),
                description = (TextView)convertView.findViewById(R.id.description);

        if (event != null) {
            title.setText(event.getName().resolve());
            location.setText(event.getLocation().getName().resolve());
            date.setText(dateFormatter.format(event.getStartTime()));
            description.setText(event.getDescription().resolve());
        }

        return convertView;
    }
}
