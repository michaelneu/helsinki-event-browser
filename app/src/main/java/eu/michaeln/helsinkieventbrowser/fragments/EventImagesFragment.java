package eu.michaeln.helsinkieventbrowser.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import eu.michaeln.helsinkieventbrowser.R;
import eu.michaeln.helsinkieventbrowser.adapters.EventImagesAdapter;

public class EventImagesFragment extends EventFragmentBase {
    public EventImagesFragment() {
        super(R.layout.fragment_event_images);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);

        final ListView images = (ListView)view.findViewById(R.id.images);
        final EventImagesAdapter adapter = new EventImagesAdapter(getContext(), event.getImages());

        images.setAdapter(adapter);

        return view;
    }
}
