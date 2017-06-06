package eu.michaeln.helsinkieventbrowser.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import eu.michaeln.helsinkieventbrowser.R;
import eu.michaeln.helsinkieventbrowser.api.VolleySingleton;
import eu.michaeln.helsinkieventbrowser.entities.Image;

public class EventImagesAdapter extends ArrayAdapter<Image> {
    public EventImagesAdapter(@NonNull Context context, Image[] images) {
        super(context, R.layout.list_item_image, images);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.list_item_image, null);
        }

        final Image image = getItem(position);

        if (image != null) {
            final NetworkImageView imageView = (NetworkImageView)convertView.findViewById(R.id.image);
            final ImageLoader imageLoader = VolleySingleton.getInstance(getContext().getApplicationContext())
                                                            .getImageLoader();

            imageView.setImageUrl(image.getUrl(), imageLoader);
        }

        return convertView;
    }
}
