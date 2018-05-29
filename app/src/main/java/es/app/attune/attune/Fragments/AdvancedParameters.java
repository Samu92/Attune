package es.app.attune.attune.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xw.repo.BubbleSeekBar;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import es.app.attune.attune.Classes.DatabaseFunctions;
import es.app.attune.attune.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdvancedParameters extends Fragment {

    private static DatabaseFunctions db;
    private BubbleSeekBar acousticness;
    private BubbleSeekBar danceability;
    private BubbleSeekBar energy;
    private BubbleSeekBar instrumentalness;
    private BubbleSeekBar liveness;
    private BubbleSeekBar loudness;
    private BubbleSeekBar popularity;
    private BubbleSeekBar speechiness;
    private BubbleSeekBar valence;

    public float getAcousticness() {
        return acousticness.getProgressFloat();
    }

    public float getDanceability() {
        return danceability.getProgressFloat();
    }

    public float getEnergy() {
        return energy.getProgressFloat();
    }

    public float getInstrumentalness() {
        return instrumentalness.getProgressFloat();
    }

    public float getLiveness() {
        return liveness.getProgressFloat();
    }

    public float getLoudness() {
        return loudness.getProgressFloat();
    }

    public int getPopularity() {
        return popularity.getProgress();
    }

    public float getSpeechiness() {
        return speechiness.getProgressFloat();
    }

    public float getValence() {
        return valence.getProgressFloat();
    }

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

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        acousticness = (BubbleSeekBar) getView().findViewById(R.id.acousticness);
        danceability = (BubbleSeekBar) getView().findViewById(R.id.danceability);
        energy = (BubbleSeekBar) getView().findViewById(R.id.energy);
        instrumentalness = (BubbleSeekBar) getView().findViewById(R.id.instrumentalness);
        liveness = (BubbleSeekBar) getView().findViewById(R.id.liveness);
        loudness = (BubbleSeekBar) getView().findViewById(R.id.loudness);
        popularity = (BubbleSeekBar) getView().findViewById(R.id.popularity);
        speechiness = (BubbleSeekBar) getView().findViewById(R.id.speachiness);
        valence = (BubbleSeekBar) getView().findViewById(R.id.valence);

        super.onViewCreated(view, savedInstanceState);
    }

    public boolean ValidarFormulario(){
        boolean valid = true;

        return valid;
    }
}
