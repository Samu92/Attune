package es.app.attune.attune.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.xw.repo.BubbleSeekBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import es.app.attune.attune.R;
import es.app.attune.attune.classes.CredentialsHandler;
import es.app.attune.attune.classes.DatabaseFunctions;
import es.app.attune.attune.classes.SearchInterfaces;
import es.app.attune.attune.database.AttPlaylist;
import es.app.attune.attune.modules.Tools;

import static android.app.Activity.RESULT_OK;

public class NewPlayList extends Fragment implements AdapterView.OnItemSelectedListener, SearchInterfaces.ResultNewPlaylist {

    private static final int PICK_IMAGE_REQUEST = 100;
    private static ConnectivityManager manager;
    private static RangeSeekBar tempo;
    private static BubbleSeekBar playlist_duration;
    private static DatabaseFunctions db;
    private static List<String> genres_list;
    private static SearchInterfaces.ActionListener mLocalActionListener;
    private static MaterialDialog progress;
    private static float mMinTempo;
    private static float mMaxTempo;
    private OnFragmentInteractionListener mListener;
    private ImageView image;
    private EditText name;
    private BubbleSeekBar song_duration;
    private CheckBox check_song_duration;
    private SearchableSpinner searchableSpinner;
    private TextView empty_genre_list;
    private TextView txt_loading;
    private TextView txt_error;
    private MaterialDialog playlist_list_dialog;
    private MaterialDialog offline;
    private TextView txt_offline;
    private ArrayAdapter<String> adapter_playlist_list;
    private ListView playlist_list_view;
    private MaterialDialog error;
    private ListView genres_list_view;
    private ArrayAdapter<String> genres_list_adapter;

    public NewPlayList() {
        // Required empty public constructor
        mMinTempo = 0;
        mMaxTempo = 0;
    }

    // TODO: Rename and change types and number of parameters
    public static NewPlayList newInstance(DatabaseFunctions database, SearchInterfaces.ActionListener mActionListener) {
        NewPlayList fragment = new NewPlayList();
        db = database;
        mLocalActionListener = mActionListener;
        return fragment;
    }

    public static float getMinTempo() {
        return mMinTempo;
    }

    public static float getMaxTempo() {
        return mMaxTempo;
    }

    public static String getCategory() {
        StringBuilder genres = new StringBuilder();
        for (String item: genres_list
             ) {
            genres.append(item).append(",");
        }
        genres = new StringBuilder(genres.substring(0, genres.length() - 1));
        return genres.toString();
    }

    public static int getDuration(){ return playlist_duration.getProgress();}

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static void stopProgress() {
        progress.dismiss();
    }

    public Bitmap getImage() {
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        bitmap = bitmap.copy(bitmap.getConfig(), false);
        return bitmap;
    }

    public String getName() {
        return name.getText().toString();
    }

    public float getSongDuration() {
        if(check_song_duration.isChecked()){
            return song_duration.getProgressFloat();
        }else{
            return 0;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        genres_list = new ArrayList<String>();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_play_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progress = new MaterialDialog.Builder(Objects.requireNonNull(getActivity()))
                .customView(R.layout.loading_layout, false)
                .cancelable(false)
                .build();

        TextView txt_loading = progress.getView().findViewById(R.id.txt_loading);
        txt_loading.setText(R.string.playlist_creating);

        error = new MaterialDialog.Builder(getActivity())
                .customView(R.layout.error_layout, false)
                .cancelable(true)
                .positiveText(R.string.agree)
                .build();

        txt_error = error.getView().findViewById(R.id.txt_error);

        image = Objects.requireNonNull(getView()).findViewById(R.id.image_playlist);
        Glide.with(Objects.requireNonNull(getContext()))
                .load(R.drawable.ic_image_gray_48dp)
                .into(image);

        name = getView().findViewById(R.id.name_playlist);

        tempo = getView().findViewById(R.id.tempo_range_seek);
        tempo.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                //leftValue is left seekbar value, rightValue is right seekbar value
                mMinTempo = leftValue;
                mMaxTempo = rightValue;
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
                //start tracking touch
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                //stop tracking touch
            }
        });
        tempo.setIndicatorTextDecimalFormat("0");
        tempo.setValue(20, 200);

        searchableSpinner = getView().findViewById(R.id.category_spinner);

        playlist_duration = getView().findViewById(R.id.duration_seekbar);

        song_duration = getView().findViewById(R.id.song_duration_seekbar);
        song_duration.setEnabled(false);
        song_duration.setSecondTrackColor(Color.GRAY);
        song_duration.setThumbColor(Color.GRAY);

        check_song_duration = getView().findViewById(R.id.check_song_duration);
        check_song_duration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                song_duration.setEnabled(b);
                if(!b){
                    song_duration.setThumbColor(Color.GRAY);
                    song_duration.setSecondTrackColor(Color.GRAY);
                    song_duration.setProgress(1);
                }else{
                    song_duration.setThumbColor(ContextCompat.getColor(getContext(), R.color.white));
                    song_duration.setSecondTrackColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener(){

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)),
                        PICK_IMAGE_REQUEST);
            }
        });

        searchableSpinner.setTitle(getString(R.string.add_genre));
        searchableSpinner.setPositiveButton(getString(R.string.accept));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, db.getAutomaticGenres());

        genres_list_view = getView().findViewById(R.id.genres_list);
        genres_list_adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, genres_list);

        genres_list_view.setAdapter(genres_list_adapter);

        genres_list_adapter.notifyDataSetChanged();

        // Apply the adapter to the spinner
        searchableSpinner.setAdapter(adapter);

        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0){
                    if(genres_list.size() <= 3){
                        String selected_item = searchableSpinner.getSelectedItem().toString();
                        if(!genres_list.contains(selected_item)){
                            genres_list.add((String) searchableSpinner.getSelectedItem());
                            genres_list_adapter.notifyDataSetChanged();
                            genres_list_view.setVisibility(View.VISIBLE);
                            empty_genre_list.setVisibility(View.GONE);
                            searchableSpinner.setSelection(0);
                            searchableSpinner.setSelected(false);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        genres_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                genres_list.remove(i);
                genres_list_adapter.notifyDataSetChanged();
                if(genres_list_adapter.isEmpty()){
                    genres_list_view.setVisibility(View.GONE);
                    empty_genre_list.setVisibility(View.VISIBLE);
                    searchableSpinner.setSelection(0);
                    searchableSpinner.setSelected(false);
                }
            }
        });

        empty_genre_list = getView().findViewById(R.id.empty_list_genre);
        if(genres_list_adapter.isEmpty()){
            genres_list_view.setVisibility(View.GONE);
            empty_genre_list.setVisibility(View.VISIBLE);
        }else{
            genres_list_view.setVisibility(View.VISIBLE);
            empty_genre_list.setVisibility(View.GONE);
        }

        ScrollView mContainer = getView().findViewById(R.id.scroll_new_playlist);
        mContainer.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                playlist_duration.correctOffsetWhenContainerOnScrolling();
                song_duration.correctOffsetWhenContainerOnScrolling();
            }
        });

        playlist_list_dialog = new MaterialDialog.Builder(getContext())
                .title(R.string.select_playlist)
                .customView(R.layout.playlist_list_short,false)
                .negativeText(R.string.disagree)
                .build();

        playlist_list_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                adapter_playlist_list = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, db.getPlaylistsNames());
                playlist_list_view.setAdapter(adapter_playlist_list);
                adapter_playlist_list.notifyDataSetChanged();
            }
        });

        assert playlist_list_dialog.getCustomView() != null;
        TextView empty_playlist_list = playlist_list_dialog.getCustomView().findViewById(R.id.empty_short_playlist_list_view);

        playlist_list_view = playlist_list_dialog.getCustomView().findViewById(R.id.short_playlist_list);
        adapter_playlist_list = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, db.getPlaylistsNames());
        playlist_list_view.setAdapter(adapter_playlist_list);

        playlist_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String playlist = adapter_playlist_list.getItem(i);
                if(db.playlistNameExists(playlist)){
                    AttPlaylist selected_playlist = db.getPlaylistByName(playlist);
                    selected_playlist.setDuration(getDuration());
                    progress.show();
                    mLocalActionListener.searchRecomendations(selected_playlist,1);
                    playlist_list_dialog.dismiss();
                }
            }
        });

        if(adapter_playlist_list.getCount() == 0){
            playlist_list_view.setVisibility(View.GONE);
            empty_playlist_list.setVisibility(View.VISIBLE);
        }else{
            playlist_list_view.setVisibility(View.VISIBLE);
            empty_playlist_list.setVisibility(View.GONE);
        }

        Button create_playlist_button = getView().findViewById(R.id.create_playlist_button);
        create_playlist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline(getContext())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.select_playlist_option)
                            .setItems(R.array.create_playlist_options, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which == 0){
                                        if(ValidarFormulario(0)){
                                            //start a new thread to process job
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    int position = db.getNextPosition();

                                                    byte[] image = Tools.getByteArray(getImage());

                                                    // Obtenemos el nombre de la nueva playlist
                                                    String name = getName();

                                                    // Obtenemos el tempo seleccionado
                                                    float min_tempo = Math.round(getMinTempo());

                                                    float max_tempo = Math.round(getMaxTempo());

                                                    // Obtenemos las categorías seleccionadas
                                                    String genre = getCategory();

                                                    // Obtenemos la duración máxima seleccionada
                                                    int duration = getDuration();

                                                    //Obtenemos la duración de cada canción
                                                    float song_duration = getSongDuration();

                                                    float acoustiness = AdvancedParameters.getAcousticness();

                                                    float danceability = AdvancedParameters.getDanceability();

                                                    float energy = AdvancedParameters.getEnergy();

                                                    float instrumentalness = AdvancedParameters.getInstrumentalness();

                                                    float liveness = AdvancedParameters.getLiveness();

                                                    float loudness = AdvancedParameters.getLoudness();

                                                    int popularity = AdvancedParameters.getPopularity();

                                                    float speechiness = AdvancedParameters.getSpeechiness();

                                                    float valence = AdvancedParameters.getValence();

                                                    // Procedemos a llamar a la API para obtener las canciones
                                                    UUID newId = java.util.UUID.randomUUID();
                                                    String user = CredentialsHandler.getUserId(getContext());
                                                    AttPlaylist newPlaylist = new AttPlaylist(newId.toString(), user, position,
                                                            name, (int) min_tempo, (int) max_tempo, duration, song_duration, image, genre, Calendar.getInstance().getTime(),
                                                            acoustiness,danceability,energy,instrumentalness,liveness,
                                                            loudness,popularity,speechiness,valence);

                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //Cambiar controles
                                                            progress.show();
                                                        }
                                                    });

                                                    mLocalActionListener.searchRecomendations(newPlaylist,0);
                                                }
                                            }).start();
                                        }
                                    }else if(which == 1){
                                        if(ValidarFormulario(1)){
                                            playlist_list_dialog.show();
                                        }
                                    }
                                }
                            });
                    builder.show();
                }else{
                    Log.e("Connection",getString(R.string.no_connection));
                    offline = new MaterialDialog.Builder(getContext())
                            .customView(R.layout.error_layout, false)
                            .cancelable(true)
                            .positiveText(R.string.agree)
                            .build();

                    txt_offline = offline.getView().findViewById(R.id.txt_error);
                    txt_offline.setText(R.string.txt_no_connection);
                    offline.show();
                }
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setAdvancedParametersFragment(AdvancedParameters fragmentAdvancedParameters) {
    }

    @Override
    public void dismissProgress() {
        progress.dismiss();
    }

    /*
        Función que valida el formulario de creación de la playlist
     */
    public boolean ValidarFormulario(int mode){
        Boolean valid = true;

        if(mode == 0){
            if(name.getText().toString().isEmpty() ){
                valid = false;
                txt_error.setText(getString(R.string.validate_name));
                error.show();
                return false;
            }else{
                if(db.playlistNameExists(name.getText().toString())){
                    valid = false;
                    txt_error.setText(getString(R.string.validate_name_exists));
                    error.show();
                    return false;
                }
            }
        }

        if(genres_list.size() == 0){
            valid = false;
            txt_error.setText(getString(R.string.validate_genres_valid));
            error.show();
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case PICK_IMAGE_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();

                    // method 1
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), selectedImage);
                        Glide.with(getContext())
                                .load(bitmap)
                                .apply(new RequestOptions()
                                .placeholder(R.drawable.music_note)
                                .centerCrop())
                                .into(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
