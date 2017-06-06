package eu.michaeln.helsinkieventbrowser.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import eu.michaeln.helsinkieventbrowser.entities.AutoCompleteItem;

public final class AutoCompleteItemAdapter extends ArrayAdapter<String> {
    public AutoCompleteItemAdapter(@NonNull Context context, ArrayList<String> items) {
        super(context, android.R.layout.simple_list_item_1, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        final String item = getItem(position);

        if (item != null) {
            final TextView text1 = (TextView)convertView.findViewById(android.R.id.text1);

            text1.setText(item);
        }

        return convertView;
    }
}
