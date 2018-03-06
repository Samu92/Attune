package es.app.attune.attune.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.app.attune.attune.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewPlayList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewPlayList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPlayList extends Fragment {

    private OnFragmentInteractionListener mListener;

    // UI
    private CircleImageView image;
    private EditText name;
    private DiscreteSeekBar tempo;
    private Spinner category;
    private Boolean valid;

    public NewPlayList() {
        // Required empty public constructor
    }

    /**
     *
     * @return A new instance of fragment NewPlayList.
     */
    // TODO: Rename and change types and number of parameters
    public static NewPlayList newInstance() {
        NewPlayList fragment = new NewPlayList();
        return fragment;
    }

    public Bitmap getImage() {
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        return bitmap;
    }

    public String getName() {
        return name.getText().toString();
    }

    public int getTempo() {
        return tempo.getProgress();
    }

    public String getCategory() {
        return category.getSelectedItem().toString();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_play_list, container, false);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image = (CircleImageView) getView().findViewById(R.id.image_playlist);
        name = (EditText) getView().findViewById(R.id.name_playlist);
        tempo = (DiscreteSeekBar) getView().findViewById(R.id.bmp_seekbar);
        category = (Spinner) getView().findViewById(R.id.category_spinner);

        Spinner staticSpinner = (Spinner) getView().findViewById(R.id.category_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.Categories,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
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
            name.setError("El nombre de la playlist es obligatorio.");
            name.requestFocus();
        }
        return valid;
    }
}
