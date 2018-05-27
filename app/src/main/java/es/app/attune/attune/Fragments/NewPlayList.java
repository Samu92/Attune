package es.app.attune.attune.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.SpotifyNativeAuthUtil;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.greenrobot.greendao.database.Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.app.attune.attune.Classes.DatabaseFunctions;
import es.app.attune.attune.Classes.ErrorSnackbar;
import es.app.attune.attune.Database.DaoSession;
import es.app.attune.attune.R;

import static android.app.Activity.RESULT_OK;

public class NewPlayList extends Fragment implements AdapterView.OnItemSelectedListener {

    private OnFragmentInteractionListener mListener;

    // UI
    private CircleImageView image;
    private EditText name;
    private DiscreteSeekBar tempo;
    private DiscreteSeekBar duration;
    private SearchableSpinner searchableSpinner;
    private Boolean valid;
    private static DatabaseFunctions db;
    private static final int PICK_IMAGE_REQUEST = 100;
    private ErrorSnackbar snackbar;

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
        return searchableSpinner.getSelectedItem().toString();
    }

    public int getDuration(){ return duration.getProgress();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        snackbar = new ErrorSnackbar(container.getRootView());
        return inflater.inflate(R.layout.fragment_new_play_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image = (CircleImageView) getView().findViewById(R.id.image_playlist);
        name = (EditText) getView().findViewById(R.id.name_playlist);
        tempo = (DiscreteSeekBar) getView().findViewById(R.id.bmp_seekbar);
        searchableSpinner = (SearchableSpinner) getView().findViewById(R.id.category_spinner);
        duration = (DiscreteSeekBar) getView().findViewById(R.id.duration_seekbar);

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
                startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"),
                        PICK_IMAGE_REQUEST);
            }
        });

        searchableSpinner.setTitle("Selecciona una categoría");
        searchableSpinner.setPositiveButton("Aceptar");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, db.getGenres());

        // Apply the adapter to the spinner
        searchableSpinner.setAdapter(adapter);

        searchableSpinner.setOnItemSelectedListener(this);

        searchableSpinner.setSelection(-1);
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
            snackbar.setSnackBarText("Por favor, póngale un nombre a tu nueva playlist");
            snackbar.showSnackBar();
            name.requestFocus();
            return valid;
        }

        if(searchableSpinner.getSelectedItemPosition() == 0){
            valid = false;
            snackbar.setSnackBarText("Por favor, seleccione una categoría");
            snackbar.showSnackBar();
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
                        image.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
