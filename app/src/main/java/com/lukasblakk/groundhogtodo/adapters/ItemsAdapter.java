package com.lukasblakk.groundhogtodo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lukasblakk.groundhogtodo.R;
import com.lukasblakk.groundhogtodo.models.Item;

import java.util.ArrayList;

/**
 * Created by lukas on 2/12/17.
 */

public class ItemsAdapter extends ArrayAdapter<Item> {
        public ItemsAdapter(Context context, ArrayList<Item> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Item item = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
            }
            // Lookup view for data population
            TextView tvText = (TextView) convertView.findViewById(R.id.tvText);
            TextView tvDueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
            TextView tvRepeat = (TextView) convertView.findViewById(R.id.tvRepeat);
            // Populate the data into the template view using the data object
            tvText.setText(item.text);
            tvDueDate.setText(item.dueDate);
            tvRepeat.setText(item.repeat);
            // Return the completed view to render on screen
            return convertView;
        }
    }
