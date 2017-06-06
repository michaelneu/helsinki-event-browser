package eu.michaeln.helsinkieventbrowser.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import eu.michaeln.helsinkieventbrowser.R;
import eu.michaeln.helsinkieventbrowser.entities.LocalizedString;
import eu.michaeln.helsinkieventbrowser.entities.Offer;

public class EventDetailsFragment extends EventFragmentBase {
    private SimpleDateFormat dateFormatter;

    public EventDetailsFragment() {
        super(R.layout.fragment_event_details);

        dateFormatter = new SimpleDateFormat("dd.MM.yyy, HH:mm", Locale.ENGLISH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);

        final Button moreInformation = (Button)view.findViewById(R.id.more_information);

        final TextView title = (TextView)view.findViewById(R.id.title),
                free = (TextView)view.findViewById(R.id.free),
                location = (TextView)view.findViewById(R.id.location),
                description = (TextView)view.findViewById(R.id.description),
                from = (TextView)view.findViewById(R.id.from),
                to = (TextView)view.findViewById(R.id.to),
                offersLabel = (TextView)view.findViewById(R.id.offers_label),
                offers = (TextView)view.findViewById(R.id.offers);

        title.setText(event.getName().resolve());
        location.setText(event.getLocation().getName().resolve());
        description.setText(event.getDescription().resolve());
        from.setText(dateFormatter.format(event.getStartTime()));

        if (event.getEndTime() == null) {
            to.setText("-");
        } else {
            to.setText(dateFormatter.format(event.getEndTime()));
        }

        final Offer[] eventOffers = event.getOffers();

        if (eventOffers.length > 0 && !eventOffers[0].isFree()) {
            free.setVisibility(View.GONE);

            final StringBuilder offerBuilder = new StringBuilder();

            for (Offer offer : eventOffers) {
                offerBuilder.append(offer.getDescription().resolve());
                offerBuilder.append(": ");
                offerBuilder.append(offer.getPrice().resolve());
                offerBuilder.append("\n");
            }

            offers.setText(offerBuilder.toString());
        } else {
            offersLabel.setVisibility(View.GONE);
        }

        final LocalizedString infoUrl = event.getInfoUrl();

        if (infoUrl == null) {
            moreInformation.setVisibility(View.GONE);
        } else if (infoUrl.resolve() != null) {
            moreInformation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Uri url = Uri.parse(infoUrl.resolve());
                    final Intent urlIntent = new Intent(Intent.ACTION_VIEW, url);

                    startActivity(urlIntent);
                }
            });
        }

        return view;
    }
}
