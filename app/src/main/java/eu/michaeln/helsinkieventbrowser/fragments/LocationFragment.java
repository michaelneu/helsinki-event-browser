package eu.michaeln.helsinkieventbrowser.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import eu.michaeln.helsinkieventbrowser.R;
import eu.michaeln.helsinkieventbrowser.adapters.DetailsPagerAdapter;
import eu.michaeln.helsinkieventbrowser.entities.Event;
import eu.michaeln.helsinkieventbrowser.parcels.EventParcel;

public class LocationFragment extends Fragment implements OnMapReadyCallback {
    private Event event;
    private View view;

    @Override
    public void setArguments(Bundle args) {
        final EventParcel parcel = args.getParcelable(DetailsPagerAdapter.ARGUMENT_EVENT);

        if (parcel != null) {
            event = parcel.getEvent();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_location, container, false);
        }

        final SupportMapFragment map = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final double[] coordinates = event.getLocation()
                                            .getPosition()
                                            .getCoordinates();

        final LatLng position = new LatLng(coordinates[1], coordinates[0]);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
        googleMap.addMarker(new MarkerOptions().position(position));
    }
}
