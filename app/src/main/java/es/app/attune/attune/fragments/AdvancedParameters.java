package es.app.attune.attune.fragments;


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

import java.util.Objects;

import es.app.attune.attune.R;
import es.app.attune.attune.classes.DatabaseFunctions;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdvancedParameters extends Fragment {

    private static BubbleSeekBar acousticness;
    private static BubbleSeekBar danceability;
    private static BubbleSeekBar energy;
    private static BubbleSeekBar instrumentalness;
    private static BubbleSeekBar liveness;
    private static BubbleSeekBar loudness;
    private static BubbleSeekBar popularity;
    private static BubbleSeekBar speechiness;
    private static BubbleSeekBar valence;

    public AdvancedParameters() {
        // Required empty public constructor
    }

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

    public static AdvancedParameters newInstance(DatabaseFunctions database) {
        AdvancedParameters fragment = new AdvancedParameters();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advanced_parameters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        acousticness = Objects.requireNonNull(getView()).findViewById(R.id.acousticness);
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

        CheckBox check_acoustiness = getView().findViewById(R.id.check_acousticness);
        check_acoustiness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    acousticness.setThumbColor(Color.GRAY);
                    acousticness.setSecondTrackColor(Color.GRAY);
                    acousticness.setProgress(0);
                    acousticness.setEnabled(b);
                }else{
                    acousticness.setThumbColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.white));
                    acousticness.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    acousticness.setEnabled(b);
                }
            }
        });
        CheckBox check_danceability = getView().findViewById(R.id.check_danceability);
        check_danceability.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    danceability.setThumbColor(Color.GRAY);
                    danceability.setSecondTrackColor(Color.GRAY);
                    danceability.setProgress(0);
                    danceability.setEnabled(false);
                }else{
                    danceability.setThumbColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.white));
                    danceability.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    danceability.setEnabled(true);
                }
            }
        });
        CheckBox check_energy = getView().findViewById(R.id.check_energy);
        check_energy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    energy.setThumbColor(Color.GRAY);
                    energy.setSecondTrackColor(Color.GRAY);
                    energy.setProgress(0);
                    energy.setEnabled(false);
                }else{
                    energy.setThumbColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.white));
                    energy.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    energy.setEnabled(true);
                }
            }
        });
        CheckBox check_instrumentalness = getView().findViewById(R.id.check_instrumentalness);
        check_instrumentalness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    instrumentalness.setThumbColor(Color.GRAY);
                    instrumentalness.setSecondTrackColor(Color.GRAY);
                    instrumentalness.setProgress(0);
                    instrumentalness.setEnabled(false);
                }else{
                    instrumentalness.setThumbColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.white));
                    instrumentalness.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    instrumentalness.setEnabled(true);
                }
            }
        });
        CheckBox check_liveness = getView().findViewById(R.id.check_liveness);
        check_liveness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    liveness.setThumbColor(Color.GRAY);
                    liveness.setSecondTrackColor(Color.GRAY);
                    liveness.setProgress(0);
                    liveness.setEnabled(false);
                }else{
                    liveness.setThumbColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.white));
                    liveness.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    liveness.setEnabled(true);
                }
            }
        });
        CheckBox check_loudness = getView().findViewById(R.id.check_loudness);
        check_loudness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    loudness.setThumbColor(Color.GRAY);
                    loudness.setSecondTrackColor(Color.GRAY);
                    loudness.setProgress(0);
                    loudness.setEnabled(false);
                }else{
                    loudness.setThumbColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.white));
                    loudness.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    loudness.setEnabled(true);
                }
            }
        });
        CheckBox check_popularity = getView().findViewById(R.id.check_popularity);
        check_popularity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    popularity.setThumbColor(Color.GRAY);
                    popularity.setSecondTrackColor(Color.GRAY);
                    popularity.setProgress(0);
                    popularity.setEnabled(false);
                }else{
                    popularity.setThumbColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.white));
                    popularity.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    popularity.setEnabled(true);
                }
            }
        });
        CheckBox check_speechiness = getView().findViewById(R.id.check_speachiness);
        check_speechiness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    speechiness.setThumbColor(Color.GRAY);
                    speechiness.setSecondTrackColor(Color.GRAY);
                    speechiness.setProgress(0);
                    speechiness.setEnabled(false);
                }else{
                    speechiness.setThumbColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.white));
                    speechiness.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    speechiness.setEnabled(true);
                }
            }
        });
        CheckBox check_valence = getView().findViewById(R.id.check_valence);
        check_valence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    valence.setThumbColor(Color.GRAY);
                    valence.setSecondTrackColor(Color.GRAY);
                    valence.setProgress(0);
                    valence.setEnabled(false);
                }else{
                    valence.setThumbColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.white));
                    valence.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                    valence.setEnabled(true);
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
        return true;
    }

    public void setNewPlaylistFragment(NewPlayList fragmentNewPlaylist) {
    }
}
