package eu.michaeln.helsinkieventbrowser.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.michaeln.helsinkieventbrowser.adapters.DetailsPagerAdapter;
import eu.michaeln.helsinkieventbrowser.entities.Event;
import eu.michaeln.helsinkieventbrowser.parcels.EventParcel;

public abstract class EventFragmentBase extends Fragment {
    private int layoutId;

    protected Event event;
    protected View view;

    protected EventFragmentBase(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public void setArguments(Bundle args) {
        if (args != null) {
            final EventParcel parcel = args.getParcelable(DetailsPagerAdapter.ARGUMENT_EVENT);

            if (parcel != null) {
                event = parcel.getEvent();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(layoutId, container, false);
        }

        return view;
    }
}
