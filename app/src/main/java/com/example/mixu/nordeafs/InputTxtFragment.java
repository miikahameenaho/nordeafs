package com.example.mixu.nordeafs;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InputTxtFragment.OnSearchQueryChangedListener} interface
 * to handle interaction events.
 * Use the {@link InputTxtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputTxtFragment extends Fragment {

    private EditText search_query_txt;

    private OnSearchQueryChangedListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InputTxtFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InputTxtFragment newInstance() {
        InputTxtFragment fragment = new InputTxtFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public InputTxtFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_input_txt, container, false);
        search_query_txt = (EditText) view.findViewById(R.id.InputSearchQuery);
        search_query_txt.addTextChangedListener(new TextChangeListener());

        return view;
    }

    private class TextChangeListener implements TextWatcher {
        @Override
        public void onTextChanged (CharSequence s,int start, int before, int count){
            //Log.d("InputTxtFragment: ", "onTextChanged: " + search_query_txt.getText().toString());
        }

        @Override
        public void beforeTextChanged (CharSequence s,int start, int count, int after){
            //Log.d("InputTxtFragment: ", "beforeTextChanged: " + search_query_txt.getText().toString());
        }

        @Override
        public void afterTextChanged (Editable s){
            Log.d("InputTxtFragment: ", "afterTextChanged: " + search_query_txt.getText().toString());
            mListener.onSearchQueryChanged(search_query_txt.getText().toString());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSearchQueryChangedListener) activity;
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
    public interface OnSearchQueryChangedListener {
        public void onSearchQueryChanged(String search_query);
    }

}
