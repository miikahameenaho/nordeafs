/*
 * Copyright (c) 2014. Miika Hämeenaho - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Miika Hämeenaho <miika.hameenaho@ovi.com>
 */

package com.example.mixu.nordeafs;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import android.os.Bundle;
import android.os.AsyncTask;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class VenueListFragment extends ListFragment {

    private static final String TAG_RESPONSE = "response";
    private static final String TAG_VENUES = "venues";
    private static final String TAG_NAME = "name";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_CITY = "city";
    private static final String TAG_FORMATTEDADDRESS = "formattedAddress";
    private static final String TAG_DISTANCE = "distance";
    private static final String TAG_CATEGORIES = "categories";
    private static final String TAG_ICON = "icon";

    // ============== App Specific keys ====================//
    final String CLIENT_ID = "3JADJIOBFTOS0K31GWODG0ADEZVPW3LM415RVM1FUWDD0WNA";
    final String CLIENT_SECRET = "KVPU54TKEYIF4ET4A3P2MZKMXGDVHMGJNDXVBGS0NCTS2BXP";

    final String QUERY_STYLE = "foursquare";
    final String QUERY_VERSION = "20140806";

    final String QUERY_START = "https://api.foursquare.com/v2/venues/search?client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&v="+QUERY_VERSION+"&m"+QUERY_STYLE;

    String search_query = null;

    String latitude = null;
    String longitude = null;

    ArrayList<FSVenue> venuesList;
    VenueAdapter myAdapter;

    private OnFragmentInteractionListener mListener;

    public static VenueListFragment newInstance() {
        VenueListFragment fragment = new VenueListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VenueListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle SavedInstance) {
        return inflater.inflate(R.layout.fragment_venue_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("VenueListFragment: ", "onCreate");
        execute(null);
    }

    public void execute(String search_query) {
        LocationTracker location = LocationTracker.getInstance(this.getActivity());

        if (search_query != null) {
            // Need to replace space with %20 for making FS api happy
            this.search_query = search_query.replaceAll(" ", "%20");
        }
        else {
            this.search_query = null;
        }

        if(location.isLocationAvailable() ){
            location.getLocation();
            latitude = Double.toString(location.getLatitude());
            longitude = Double.toString(location.getLongitude());
        }else{
            Log.e("VenueListFragment: ", "No GPS signal available");
            // TODO show error
            //text.setText("No GPS signal available");
            location.showSettingsAlert();
        }

        // Gets the URL from the UI's text field.
        ConnectivityManager connMgr = (ConnectivityManager)
                this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new asyncFs().execute();
        } else {
            Log.e("VenueListFragment: ", "No network connection available");
            // TODO show error
            //text.setText("No network connection available.");
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // TODO Not in use currently

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
//            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Not currently needed
        public void onFragmentInteraction(String id);
    }

    private class asyncFs extends AsyncTask<View, Void, String> {
        String temp;

        @Override
        protected String doInBackground(View... urls) {
            String url;
            /* If search query has been defined we use that, otherwise we search all the nearest
             * venues
             */
            if (search_query != null) {
                url = QUERY_START + "&query=" + search_query + "&ll=" + latitude + "," + longitude;
            }
            else {
                url = QUERY_START + "&ll=" + latitude + "," + longitude;
            }

            Log.d("VenueListFragment: ", "doInBackground query: " +url);
            temp = makeCall(url);
            Log.d("VenueListFragment: ", "doInBackground data returned: " +temp);
            return "";
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }

        @Override
        protected void onPostExecute(String result) {
            if (temp != null) {
                // parseFSData venues search result
                venuesList = parseFSData(temp);

                myAdapter = new VenueAdapter(getActivity(), R.layout.row_layout, venuesList);
                setListAdapter(myAdapter);
            }
        }
    }

    public static String makeCall(String url) {
        StringBuffer buffer_string = new StringBuffer(url);
        String replyString = "";

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(buffer_string.toString());

        InputStream is = null;
        try {
            HttpResponse response = httpclient.execute(httpget);
            is = response.getEntity().getContent();

            // buffer input stream the result
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            // the result as a string is ready for parsing
            replyString = new String(baf.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // trim the whitespaces
        return replyString.trim();
    }

    private static ArrayList<FSVenue> parseFSData(final String response) {
        ArrayList<FSVenue> temp = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.has(TAG_RESPONSE)) {
                if (jsonObject.getJSONObject(TAG_RESPONSE).has(TAG_VENUES)) {
                    JSONArray jsonArray = jsonObject.getJSONObject(TAG_RESPONSE).getJSONArray(TAG_VENUES);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        FSVenue poi = new FSVenue();
                        if (jsonArray.getJSONObject(i).has(TAG_NAME)) {
                            poi.setName(jsonArray.getJSONObject(i).getString(TAG_NAME));
                        }
                        if (jsonArray.getJSONObject(i).has(TAG_LOCATION)) {
                            if (jsonArray.getJSONObject(i).getJSONObject(TAG_LOCATION).has(TAG_CITY)) {
                                poi.setCity(jsonArray.getJSONObject(i).getJSONObject(TAG_LOCATION).getString(TAG_CITY));
                            }
                            if (jsonArray.getJSONObject(i).getJSONObject(TAG_LOCATION).has(TAG_FORMATTEDADDRESS)) {
                                poi.setFormattedAddress(jsonArray.getJSONObject(i).getJSONObject(TAG_LOCATION).getString(TAG_FORMATTEDADDRESS));
                            }
                            if (jsonArray.getJSONObject(i).getJSONObject(TAG_LOCATION).has(TAG_DISTANCE)) {
                                poi.setDistance(jsonArray.getJSONObject(i).getJSONObject(TAG_LOCATION).getString(TAG_DISTANCE));
                            }
                        }
                        if ((jsonArray.getJSONObject(i).has(TAG_CATEGORIES)) && (jsonArray.getJSONObject(i).getJSONArray(TAG_CATEGORIES).length() > 0)) {
                            if (jsonArray.getJSONObject(i).getJSONArray(TAG_CATEGORIES).getJSONObject(0).has(TAG_NAME)) {
                                poi.setCategory(jsonArray.getJSONObject(i).getJSONArray(TAG_CATEGORIES).getJSONObject(0).getString(TAG_NAME));
                            }
                            if (jsonArray.getJSONObject(i).getJSONArray(TAG_CATEGORIES).getJSONObject(0).has(TAG_ICON)) {
                                poi.setIconUrl(jsonArray.getJSONObject(i).getJSONArray(TAG_CATEGORIES).getJSONObject(0).getString(TAG_ICON));
                            }

                        }
                        temp.add(poi);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return temp;
    }
}