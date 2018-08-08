package es.app.attune.attune.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;

import com.xw.repo.BubbleSeekBar;

import java.util.Date;

import es.app.attune.attune.Classes.DatabaseFunctions;
import es.app.attune.attune.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdvancedParameters extends Fragment {

    private static DatabaseFunctions db;
    private static BubbleSeekBar acousticness;
    private static BubbleSeekBar danceability;
    private static BubbleSeekBar energy;
    private static BubbleSeekBar instrumentalness;
    private static BubbleSeekBar liveness;
    private static BubbleSeekBar loudness;
    private static BubbleSeekBar popularity;
    private static BubbleSeekBar speechiness;
    private static BubbleSeekBar valence;
    private CheckBox check_acoustiness;
    private CheckBox check_danceability;
    private CheckBox check_energy;
    private CheckBox check_instrumentalness;
    private CheckBox check_liveness;
    private CheckBox check_loudness;
    private CheckBox check_popularity;
    private CheckBox check_speechiness;
    private CheckBox check_valence;
    private NewPlayList newPlaylistFragment;

    public static float getAcousticness() {
        if(acousticness.isEnabled()) {
            return acousticness.getProgressFloat();
        }
        else{
            return -1;
        }
    }

    public static float getDanceability() {
        if(danceability.isEnabled()) {
            return danceability.getProgressFloat();
        }
        else{
            return -1;
        }
    }

    public static float getEnergy() {
        if(energy.isEnabled()){
            return energy.getProgressFloat();
        }else{
            return -1;
        }
    }

    public static float getInstrumentalness() {
        if(instrumentalness.isEnabled()){
            return instrumentalness.getProgressFloat();
        }else{
            return -1;
        }
    }

    public static float getLiveness() {
        if(liveness.isEnabled()){
            return liveness.getProgressFloat();
        }else{
            return -1;
        }
    }

    public static float getLoudness() {
        if(loudness.isEnabled()){
            return loudness.getProgressFloat();
        }else{
            return -1;
        }
    }

    public static int getPopularity() {
        if(popularity.isEnabled()){
            return popularity.getProgress();
        }else{
            return -1;
        }
    }

    public static float getSpeechiness() {
        if(speechiness.isEnabled()){
            return speechiness.getProgressFloat();
        }else{
            return -1;
        }
    }

    public static float getValence() {
        if(valence.isEnabled()){
            return valence.getProgressFloat();
        }else{
            return -1;
        }
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        acousticness = getView().findViewById(R.id.acousticness);
        acousticness.setThumbColor(Color.GRAY);
        acousticness.setSecondTrackColor(Color.GRAY);
        acousticness.setProgress(0);
        acousticness.setEnabled(false);
        acousticness.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
                array.clear();
                array.put(0, getString(R.string.low_fem));
                array.put(sectionCount, getString(R.string.high_fem));
                return array;
            }
        });

        danceability = getView().findViewById(R.id.danceability);
        danceability.setThumbColor(Color.GRAY);
        danceability.setSecondTrackColor(Color.GRAY);
        danceability.setProgress(0);
        danceability.setEnabled(false);
        danceability.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
                array.clear();
                array.put(0,getString(R.string.low_fem));
                array.put(sectionCount,getString(R.string.high_fem));
                return array;
            }
        });

        energy = getView().findViewById(R.id.energy);
        energy.setThumbColor(Color.GRAY);
        energy.setSecondTrackColor(Color.GRAY);
        energy.setProgress(0);
        energy.setEnabled(false);
        energy.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
                array.clear();
                array.put(0,getString(R.string.low_fem));
                array.put(sectionCount,getString(R.string.high_fem));
                return array;
            }
        });

        instrumentalness = getView().findViewById(R.id.instrumentalness);
        instrumentalness.setThumbColor(Color.GRAY);
        instrumentalness.setSecondTrackColor(Color.GRAY);
        instrumentalness.setProgress(0);
        instrumentalness.setEnabled(false);
        instrumentalness.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
                array.clear();
                array.put(0,getString(R.string.low_fem));
                array.put(sectionCount,getString(R.string.high_fem));
                return array;
            }
        });

        liveness = getView().findViewById(R.id.liveness);
        liveness.setThumbColor(Color.GRAY);
        liveness.setSecondTrackColor(Color.GRAY);
        liveness.setProgress(0);
        liveness.setEnabled(false);
        liveness.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
                array.clear();
                array.put(0,getString(R.string.low_fem));
                array.put(sectionCount,getString(R.string.high_fem));
                return array;
            }
        });

        loudness = getView().findViewById(R.id.loudness);
        loudness.setThumbColor(Color.GRAY);
        loudness.setSecondTrackColor(Color.GRAY);
        loudness.setProgress(0);
        loudness.setEnabled(false);
        loudness.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
                array.clear();
                array.put(0,getString(R.string.low_fem));
                array.put(sectionCount,getString(R.string.high_fem));
                return array;
            }
        });

        popularity = getView().findViewById(R.id.popularity);
        popularity.setThumbColor(Color.GRAY);
        popularity.setSecondTrackColor(Color.GRAY);
        popularity.setProgress(0);
        popularity.setEnabled(false);
        popularity.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
                array.clear();
                array.put(0,getString(R.string.low_fem));
                array.put(sectionCount,getString(R.string.high_fem));
                return array;
            }
        });

        speechiness = getView().findViewById(R.id.speachiness);
        speechiness.setThumbColor(Color.GRAY);
        speechiness.setSecondTrackColor(Color.GRAY);
        speechiness.setProgress(0);
        speechiness.setEnabled(false);
        speechiness.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
                array.clear();
                array.put(0,getString(R.string.low_fem));
                array.put(sectionCount,getString(R.string.high_fem));
                return array;
            }
        });

        valence = getView().findViewById(R.id.valence);
        valence.setThumbColor(Color.GRAY);
        valence.setSecondTrackColor(Color.GRAY);
        valence.setProgress(0);
        valence.setEnabled(false);
        valence.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
                array.clear();
                array.put(0,getString(R.string.low_masc));
                array.put(sectionCount,getString(R.string.high_masc));
                return array;
            }
        });

        check_acoustiness = getView().findViewById(R.id.check_acousticness);
        check_acoustiness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    acousticness.setThumbColor(Color.GRAY);
                    acousticness.setSecondTrackColor(Color.GRAY);
                    acousticness.setProgress(0);
                    acousticness.setEnabled(b);
                }else{
                    acousticness.setThumbColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    acousticness.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    acousticness.setEnabled(b);
                }
            }
        });
        check_danceability = getView().findViewById(R.id.check_danceability);
        check_danceability.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    danceability.setThumbColor(Color.GRAY);
                    danceability.setSecondTrackColor(Color.GRAY);
                    danceability.setProgress(0);
                    danceability.setEnabled(b);
                }else{
                    danceability.setThumbColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    danceability.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    danceability.setEnabled(b);
                }
            }
        });
        check_energy = getView().findViewById(R.id.check_energy);
        check_energy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    energy.setThumbColor(Color.GRAY);
                    energy.setSecondTrackColor(Color.GRAY);
                    energy.setProgress(0);
                    energy.setEnabled(b);
                }else{
                    energy.setThumbColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    energy.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    energy.setEnabled(b);
                }
            }
        });
        check_instrumentalness = getView().findViewById(R.id.check_instrumentalness);
        check_instrumentalness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    instrumentalness.setThumbColor(Color.GRAY);
                    instrumentalness.setSecondTrackColor(Color.GRAY);
                    instrumentalness.setProgress(0);
                    instrumentalness.setEnabled(b);
                }else{
                    instrumentalness.setThumbColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    instrumentalness.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    instrumentalness.setEnabled(b);
                }
            }
        });
        check_liveness = getView().findViewById(R.id.check_liveness);
        check_liveness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    liveness.setThumbColor(Color.GRAY);
                    liveness.setSecondTrackColor(Color.GRAY);
                    liveness.setProgress(0);
                    liveness.setEnabled(b);
                }else{
                    liveness.setThumbColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    liveness.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    liveness.setEnabled(b);
                }
            }
        });
        check_loudness = getView().findViewById(R.id.check_loudness);
        check_loudness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    loudness.setThumbColor(Color.GRAY);
                    loudness.setSecondTrackColor(Color.GRAY);
                    loudness.setProgress(0);
                    loudness.setEnabled(b);
                }else{
                    loudness.setThumbColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    loudness.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    loudness.setEnabled(b);
                }
            }
        });
        check_popularity = getView().findViewById(R.id.check_popularity);
        check_popularity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    popularity.setThumbColor(Color.GRAY);
                    popularity.setSecondTrackColor(Color.GRAY);
                    popularity.setProgress(0);
                    popularity.setEnabled(b);
                }else{
                    popularity.setThumbColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    popularity.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    popularity.setEnabled(b);
                }
            }
        });
        check_speechiness = getView().findViewById(R.id.check_speachiness);
        check_speechiness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    speechiness.setThumbColor(Color.GRAY);
                    speechiness.setSecondTrackColor(Color.GRAY);
                    speechiness.setProgress(0);
                    speechiness.setEnabled(b);
                }else{
                    speechiness.setThumbColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    speechiness.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    speechiness.setEnabled(b);
                }
            }
        });
        check_valence = getView().findViewById(R.id.check_valence);
        check_valence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    valence.setThumbColor(Color.GRAY);
                    valence.setSecondTrackColor(Color.GRAY);
                    valence.setProgress(0);
                    valence.setEnabled(b);
                }else{
                    valence.setThumbColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    valence.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    valence.setEnabled(b);
                }
            }
        });

        ScrollView mContainer = getView().findViewById(R.id.scroll_advanced_parameters);
        mContainer.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                acousticness.correctOffsetWhenContainerOnScrolling();
                danceability.correctOffsetWhenContainerOnScrolling();
                energy.correctOffsetWhenContainerOnScrolling();
                instrumentalness.correctOffsetWhenContainerOnScrolling();
                liveness.correctOffsetWhenContainerOnScrolling();
                loudness.correctOffsetWhenContainerOnScrolling();
                popularity.correctOffsetWhenContainerOnScrolling();
                speechiness.correctOffsetWhenContainerOnScrolling();
                valence.correctOffsetWhenContainerOnScrolling();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    public boolean ValidarFormulario(int mode){
        boolean valid = true;

        return valid;
    }

    public void setNewPlaylistFragment(NewPlayList fragmentNewPlaylist) {
        this.newPlaylistFragment = fragmentNewPlaylist;
    }
}
