package eu.michaeln.helsinkieventbrowser.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import eu.michaeln.helsinkieventbrowser.fragments.EventDetailsFragment;

public class DetailsPagerAdapter extends FragmentPagerAdapter {
    private final static int FRAGMENT_DETAILS = 0,
                             FRAGMENT_LOCATION = 1,
                             FRAGMENT_IMAGES = 2;

    private final String id;

    public DetailsPagerAdapter(FragmentManager fm, String id) {
        super(fm);

        this.id = id;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FRAGMENT_LOCATION:
                return new EventDetailsFragment();

            case FRAGMENT_IMAGES:
                return new EventDetailsFragment();

            default:
                return new EventDetailsFragment();
        }
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
