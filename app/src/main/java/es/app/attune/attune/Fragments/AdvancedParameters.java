package es.app.attune.attune.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xw.repo.BubbleSeekBar;

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

    private CheckBox check_acoustiness;
    private CheckBox check_danceability;
    private CheckBox check_energy;
    private CheckBox check_instrumentalness;
    private CheckBox check_liveness;
    private CheckBox check_loudness;
    private CheckBox check_popularity;
    private CheckBox check_speechiness;
    private CheckBox check_valence;


    public float getAcousticness() {
        if(acousticness.isEnabled()) {
            return acousticness.getProgressFloat();
        }
        else{
            return -1;
        }
    }

    public float getDanceability() {
        if(danceability.isEnabled()) {
            return danceability.getProgressFloat();
        }
        else{
            return -1;
        }
    }

    public float getEnergy() {
        if(energy.isEnabled()){
            return energy.getProgressFloat();
        }else{
            return -1;
        }
    }

    public float getInstrumentalness() {
        if(instrumentalness.isEnabled()){
            return instrumentalness.getProgressFloat();
        }else{
            return -1;
        }
    }

    public float getLiveness() {
        if(liveness.isEnabled()){
            return liveness.getProgressFloat();
        }else{
            return -1;
        }
    }

    public float getLoudness() {
        if(loudness.isEnabled()){
            return loudness.getProgressFloat();
        }else{
            return -1;
        }
    }

    public int getPopularity() {
        if(popularity.isEnabled()){
            return popularity.getProgress();
        }else{
            return -1;
        }
    }

    public float getSpeechiness() {
        if(speechiness.isEnabled()){
            return speechiness.getProgressFloat();
        }else{
            return -1;
        }
    }

    public float getValence() {
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

        acousticness = (BubbleSeekBar) getView().findViewById(R.id.acousticness);
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

        danceability = (BubbleSeekBar) getView().findViewById(R.id.danceability);
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

        energy = (BubbleSeekBar) getView().findViewById(R.id.energy);
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

        instrumentalness = (BubbleSeekBar) getView().findViewById(R.id.instrumentalness);
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

        liveness = (BubbleSeekBar) getView().findViewById(R.id.liveness);
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

        loudness = (BubbleSeekBar) getView().findViewById(R.id.loudness);
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

        popularity = (BubbleSeekBar) getView().findViewById(R.id.popularity);
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

        speechiness = (BubbleSeekBar) getView().findViewById(R.id.speachiness);
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

        valence = (BubbleSeekBar) getView().findViewById(R.id.valence);
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

        check_acoustiness = (CheckBox) getView().findViewById(R.id.check_acousticness);
        check_acoustiness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                acousticness.setEnabled(b);
                acousticness.setProgress(0);
            }
        });
        check_danceability = (CheckBox) getView().findViewById(R.id.check_danceability);
        check_danceability.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                danceability.setEnabled(b);
                danceability.setProgress(0);
            }
        });
        check_energy = (CheckBox) getView().findViewById(R.id.check_energy);
        check_energy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                energy.setEnabled(b);
                energy.setProgress(0);
            }
        });
        check_instrumentalness = (CheckBox) getView().findViewById(R.id.check_instrumentalness);
        check_instrumentalness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                instrumentalness.setEnabled(b);
                instrumentalness.setProgress(0);
            }
        });
        check_liveness = (CheckBox) getView().findViewById(R.id.check_liveness);
        check_liveness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                liveness.setEnabled(b);
                liveness.setProgress(0);
            }
        });
        check_loudness = (CheckBox) getView().findViewById(R.id.check_loudness);
        check_loudness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                loudness.setEnabled(b);
                loudness.setProgress(0);
            }
        });
        check_popularity = (CheckBox) getView().findViewById(R.id.check_popularity);
        check_popularity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                popularity.setEnabled(b);
                popularity.setProgress(0);
            }
        });
        check_speechiness = (CheckBox) getView().findViewById(R.id.check_speachiness);
        check_speechiness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                speechiness.setEnabled(b);
                speechiness.setProgress(0);
            }
        });
        check_valence = (CheckBox) getView().findViewById(R.id.check_valence);
        check_valence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                valence.setEnabled(b);
                valence.setProgress(0);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    public boolean ValidarFormulario(){
        boolean valid = true;

        return valid;
    }
}
