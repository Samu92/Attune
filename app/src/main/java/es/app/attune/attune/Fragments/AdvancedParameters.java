package es.app.attune.attune.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.app.attune.attune.Classes.DatabaseFunctions;
import es.app.attune.attune.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdvancedParameters extends Fragment {

    private static DatabaseFunctions db;

    public AdvancedParameters() {
        // Required empty public constructor
    }

    public static AdvancedParameters newInstance(DatabaseFunctions database) {
        AdvancedParameters fragment = new AdvancedParameters();
        db = database;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advanced_parameters, container, false);
    }

}
