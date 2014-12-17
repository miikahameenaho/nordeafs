/*
 * Copyright (c) 2014. Miika Hämeenaho - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Miika Hämeenaho <miika.hameenaho@ovi.com>
 */

package com.example.mixu.nordeafs;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class VenueAdapter extends ArrayAdapter<FSVenue>{

    Context context;
    int layoutResourceId;
    ArrayList<FSVenue> data = null;

    public VenueAdapter(Context context, int layoutResourceId, ArrayList<FSVenue> data /*FSVenue[] data*/) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        VenueHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new VenueHolder();
            holder.venue_name = (TextView)row.findViewById(R.id.venueNameText);
            holder.venue_distance = (TextView)row.findViewById(R.id.venueDistanceText);
            holder.venue_address = (TextView)row.findViewById(R.id.venueAddressText);

            row.setTag(holder);
        }
        else
        {
            holder = (VenueHolder)row.getTag();
        }

        FSVenue venue = data.get(position);
        holder.venue_name.setText(venue.getName());
        holder.venue_distance.setText(venue.getDistance());
        holder.venue_address.setText(venue.getFormattedAddress());

        return row;
    }

    static class VenueHolder
    {
        TextView venue_name;
        TextView venue_distance;
        TextView venue_address;
    }
}
