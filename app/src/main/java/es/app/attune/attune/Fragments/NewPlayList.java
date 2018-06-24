package es.app.attune.attune.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.xw.repo.BubbleSeekBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.app.attune.attune.Classes.DatabaseFunctions;
import es.app.attune.attune.R;

import static android.app.Activity.RESULT_OK;

public class NewPlayList extends Fragment implements AdapterView.OnItemSelectedListener {

    private OnFragmentInteractionListener mListener;

    // UI
    private ImageView image;
    private EditText name;
    private BubbleSeekBar tempo;
    private BubbleSeekBar playlist_duration;
    private BubbleSeekBar song_duration;
    private CheckBox check_song_duration;
    private SearchableSpinner searchableSpinner;
    private Boolean valid;
    private static DatabaseFunctions db;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static List<String> genres_list;
    private TextView empty_genre_list;

    public NewPlayList() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewPlayList newInstance(DatabaseFunctions database) {
        NewPlayList fragment = new NewPlayList();
        db = database;
        return fragment;
    }

    public Bitmap getImage() {
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        bitmap = bitmap.copy(bitmap.getConfig(),false);
        return bitmap;
    }

    public String getName() {
        return name.getText().toString();
    }

    public int getTempo() {
        return tempo.getProgress();
    }

    public String getCategory() {
        String genres = "";
        for (String item: genres_list
             ) {
            genres += item + ",";
        }
        genres = genres.substring(0,genres.length() - 1);
        return genres;
    }

    public int getDuration(){ return playlist_duration.getProgress();}

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_play_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image = (ImageView) getView().findViewById(R.id.image_playlist);
        Glide.with(getContext())
                .load(R.drawable.baseline_add_photo_alternate_white_48)
                .into(image);

        name = (EditText) getView().findViewById(R.id.name_playlist);

        tempo = (BubbleSeekBar) getView().findViewById(R.id.bmp_seekbar);

        searchableSpinner = (SearchableSpinner) getView().findViewById(R.id.category_spinner);

        playlist_duration = (BubbleSeekBar) getView().findViewById(R.id.duration_seekbar);

        song_duration = (BubbleSeekBar) getView().findViewById(R.id.song_duration_seekbar);
        song_duration.setEnabled(false);
        song_duration.setSecondTrackColor(Color.GRAY);
        song_duration.setThumbColor(Color.GRAY);

        check_song_duration = (CheckBox) getView().findViewById(R.id.check_song_duration);
        check_song_duration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                song_duration.setEnabled(b);
                if(b == false){
                    song_duration.setThumbColor(Color.GRAY);
                    song_duration.setSecondTrackColor(Color.GRAY);
                    song_duration.setProgress(1);
                }else{
                    song_duration.setThumbColor(ContextCompat.getColor(getContext() ,R.color.colorAccent));
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

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, db.getGenres());

        final ListView genres_list_view = (ListView) getView().findViewById(R.id.genres_list);
        final ArrayAdapter<String> genres_list_adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, genres_list);

        genres_list_view.setAdapter(genres_list_adapter);

        // Apply the adapter to the spinner
        searchableSpinner.setAdapter(adapter);

        searchableSpinner.setOnItemSelectedListener(this);

        searchableSpinner.setSelection(-1);

        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0){
                    if(genres_list.size() <= 3){
                        if(!genres_list.contains((String) searchableSpinner.getSelectedItem())){
                            genres_list.add((String) searchableSpinner.getSelectedItem());
                            genres_list_adapter.notifyDataSetChanged();
                            genres_list_view.setVisibility(View.VISIBLE);
                            empty_genre_list.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        empty_genre_list = (TextView) getView().findViewById(R.id.empty_list_genre);
        if(genres_list_adapter.isEmpty()){
            genres_list_view.setVisibility(View.GONE);
            empty_genre_list.setVisibility(View.VISIBLE);
        }else{
            genres_list_view.setVisibility(View.VISIBLE);
            empty_genre_list.setVisibility(View.GONE);
        }

        genres_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                genres_list.remove(i);
                genres_list_adapter.notifyDataSetChanged();
                if(genres_list_adapter.isEmpty()){
                    genres_list_view.setVisibility(View.GONE);
                    empty_genre_list.setVisibility(View.VISIBLE);
                }
            }
        });

        ScrollView mContainer = (ScrollView) getView().findViewById(R.id.scroll_new_playlist);
        mContainer.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                tempo.correctOffsetWhenContainerOnScrolling();
                playlist_duration.correctOffsetWhenContainerOnScrolling();
                song_duration.correctOffsetWhenContainerOnScrolling();
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
        //Toast.makeText(getContext(),searchableSpinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /*
        Función que valida el formulario de creación de la playlist
     */
    public boolean ValidarFormulario(){
        valid = true;

        if(name.getText().toString().isEmpty() ){
            valid = false;
            name.requestFocus();
            return valid;
        }

        if(searchableSpinner.getSelectedItemPosition() == 0){
            valid = false;
            searchableSpinner.requestFocus();
            return valid;
        }

        return valid;
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
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
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
}
