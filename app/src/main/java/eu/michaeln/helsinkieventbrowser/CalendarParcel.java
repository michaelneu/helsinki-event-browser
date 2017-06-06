package eu.michaeln.helsinkieventbrowser;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Calendar;

public class CalendarParcel implements Parcelable {
    private Calendar calendar;

    public CalendarParcel(@NonNull Calendar calendar) {
        this.calendar = calendar;
    }

    protected CalendarParcel(Parcel in) {
        final long time = in.readLong();

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
    }

    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(calendar.getTimeInMillis());
    }

    public static final Creator<CalendarParcel> CREATOR = new Creator<CalendarParcel>() {
        @Override
        public CalendarParcel createFromParcel(Parcel in) {
            return new CalendarParcel(in);
        }

        @Override
        public CalendarParcel[] newArray(int size) {
            return new CalendarParcel[size];
        }
    };
}
