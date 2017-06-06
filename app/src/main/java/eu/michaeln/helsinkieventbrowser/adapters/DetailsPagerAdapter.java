package eu.michaeln.helsinkieventbrowser.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import eu.michaeln.helsinkieventbrowser.entities.Event;
import eu.michaeln.helsinkieventbrowser.fragments.EventDetailsFragment;
import eu.michaeln.helsinkieventbrowser.fragments.EventLocationFragment;
import eu.michaeln.helsinkieventbrowser.parcels.EventParcel;

public class DetailsPagerAdapter extends FragmentPagerAdapter {
    public static final String ARGUMENT_EVENT = "event";

    private static final int FRAGMENT_DETAILS = 0,
                             FRAGMENT_IMAGES = 1,
                             FRAGMENT_LOCATION = 2;

    private final Event event;

    public DetailsPagerAdapter(FragmentManager fm, Event event) {
        super(fm);

        this.event = event;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        switch (position) {
            case FRAGMENT_LOCATION:
                fragment = new EventLocationFragment();
                break;

            case FRAGMENT_IMAGES:
                fragment = new EventDetailsFragment();
                break;

            default:
                fragment = new EventDetailsFragment();
                break;
        }

        final Bundle arguments = new Bundle();

        arguments.putParcelable(ARGUMENT_EVENT, new EventParcel(event));

        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case FRAGMENT_LOCATION:
                return "Location";

            case FRAGMENT_IMAGES:
                return "Images";

            default:
                return "Event";
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
