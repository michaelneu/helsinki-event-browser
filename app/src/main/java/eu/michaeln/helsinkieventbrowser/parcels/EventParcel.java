package eu.michaeln.helsinkieventbrowser.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import eu.michaeln.helsinkieventbrowser.entities.Event;

public class EventParcel implements Parcelable {
    private Event event;

    public EventParcel(Event event) {
        this.event = event;
    }

    protected EventParcel(Parcel in) {
        final Gson gson = new Gson();
        final String eventJson = in.readString();

        this.event = gson.fromJson(eventJson, Event.class);
    }

    public static final Creator<EventParcel> CREATOR = new Creator<EventParcel>() {
        @Override
        public EventParcel createFromParcel(Parcel in) {
            return new EventParcel(in);
        }

        @Override
        public EventParcel[] newArray(int size) {
            return new EventParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        final Gson gson = new Gson();
        final String eventJson = gson.toJson(event);

        parcel.writeString(eventJson);
    }

    public Event getEvent() {
        return event;
    }
}
