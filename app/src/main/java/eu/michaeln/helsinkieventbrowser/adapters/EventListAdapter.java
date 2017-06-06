package eu.michaeln.helsinkieventbrowser.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import eu.michaeln.helsinkieventbrowser.R;
import eu.michaeln.helsinkieventbrowser.entities.Event;
import eu.michaeln.helsinkieventbrowser.entities.Location;

public final class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, location, date, description;
        private RelativeLayout content;
        private ImageView icon;

        public ViewHolder(View view) {
            super(view);

            content = (RelativeLayout)view.findViewById(R.id.content);

            title = (TextView)view.findViewById(R.id.title);
            location = (TextView)view.findViewById(R.id.location);
            date = (TextView)view.findViewById(R.id.date);
            description = (TextView)view.findViewById(R.id.description);

            icon = (ImageView)view.findViewById(R.id.icon);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getLocation() {
            return location;
        }

        public TextView getDate() {
            return date;
        }

        public TextView getDescription() {
            return description;
        }

        public RelativeLayout getContent() {
            return content;
        }

        public ImageView getIcon() {
            return icon;
        }
    }

    private final AdapterView.OnItemClickListener listener;
    private final SimpleDateFormat dateFormatter;
    private Event[] events;

    public EventListAdapter(Event[] data, AdapterView.OnItemClickListener clickListener) {
        listener = clickListener;
        events = data;
        dateFormatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.ENGLISH);
    }

    public void setEvents(Event[] data) {
        events = data;

        notifyDataSetChanged();
    }

    public Event[] getEvents() {
        return events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.list_item_event, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(null, holder.itemView, position, 0);
            }
        });

        final Event event = events[position];

        if (event != null) {
            holder.getContent().setVisibility(View.VISIBLE);

            holder.getTitle().setText(event.getName().resolve());

            final Location location = event.getLocation();

            if (location != null && location.getName() != null) {
                holder.getLocation().setText(location.getName().resolve());
            } else {
                holder.getLocation().setText("");
            }

            holder.getDate().setText(dateFormatter.format(event.getStartTime()));
            holder.getDescription().setText(event.getDescription().resolve());

            holder.getIcon().setVisibility(View.GONE);
        } else {
            holder.getContent().setVisibility(View.GONE);
            holder.getIcon().setVisibility(View.VISIBLE);

            if (position == 0) {
                holder.getIcon().setImageResource(R.drawable.ic_expand_less_black_48dp);
            } else {
                holder.getIcon().setImageResource(R.drawable.ic_expand_more_black_48dp);
            }
        }
    }

    @Override
    public int getItemCount() {
        return events.length;
    }
}
