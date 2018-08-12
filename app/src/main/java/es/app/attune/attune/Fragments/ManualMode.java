package es.app.attune.attune.Fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.xw.repo.BubbleSeekBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import es.app.attune.attune.Adapters.FilterAdapter;
import es.app.attune.attune.Adapters.SearchResultsAdapter;
import es.app.attune.attune.Classes.CredentialsHandler;
import es.app.attune.attune.Classes.DatabaseFunctions;
import es.app.attune.attune.Classes.ResultListScrollListener;
import es.app.attune.attune.Classes.SearchInterfaces;
import es.app.attune.attune.Database.AttPlaylist;
import es.app.attune.attune.Database.Song;
import es.app.attune.attune.Modules.Tools;
import es.app.attune.attune.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManualMode extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static ConnectivityManager manager;
    private static DatabaseFunctions db;
    private SearchView searchView;
    private static SearchInterfaces.ActionListener mActionListener;
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";
    private static ScrollListener mScrollListener;
    private ManualMode.OnListFragmentInteractionListener mListener;
    private static SearchResultsAdapter mAdapter;
    private static RecyclerView resultsList;
    private MaterialDialog material;
    private SearchableSpinner genresSpinner;
    private EditText editText_start;
    private EditText editText_end;
    private int year_start;
    private int year_end;
    private String genre;
    private float mTempoFilter;
    private static TextView empty;
    private static ProgressBar progressManualBar;
    private ListView playlist_list_view;
    private MaterialDialog playlist_list_dialog;
    private MaterialDialog new_playlist_dialog;
    private MaterialDialog result;
    private AppCompatCheckBox filter_date_check;
    private RecyclerView filter_list;
    private ArrayList<String> filter_array;
    private FilterAdapter filterAdapter;
    private TextView empty_filter_list;
    private Song selected_song;
    private static final int PICK_IMAGE_REQUEST = 100;
    private ImageView image;
    private TextView txt_result;
    private ArrayAdapter<String> adapter_playlist_list;
    private MaterialDialog offline;
    private TextView txt_offline;
    private AppCompatCheckBox filter_tempo_check;
    private BubbleSeekBar filter_tempo;

    public ManualMode() {
        // Required empty public constructor
    }

    public void setAdapter(List<Song> items) {
        if(mAdapter != null){
            mAdapter.addData(items);

            if(mAdapter.getItemCount() == 0){
                resultsList.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
            }else{
                resultsList.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
            }
            progressManualBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        int fecha_edicion = 0;
        if(fecha_edicion == 0){
            year_start = i;
            editText_start.setText(String.valueOf(i));
        }

        if(fecha_edicion == 1){
            year_end = i;
            editText_end.setText(String.valueOf(i));
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static void stopProgressBar() {
        progressManualBar.setVisibility(View.GONE);
    }

    private class ScrollListener extends ResultListScrollListener {

        public ScrollListener(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore() {
            if(isOnline(getContext())){
                progressManualBar.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
                mActionListener.loadMoreResults();
            }else{
                Log.e("Connection", getString(R.string.no_connection));
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
    }

    public static ManualMode newInstance(DatabaseFunctions database, SearchInterfaces.ActionListener mActionListenerArg) {
        ManualMode fragment = new ManualMode();
        db = database;
        mActionListener = mActionListenerArg;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        List<String> genres_list = new ArrayList<String>();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manual_mode, container, false);
    }

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SongsListFragment.OnListFragmentInteractionListener) {
            mListener = (ManualMode.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressManualBar = getView().findViewById(R.id.search_manual_progress);
        progressManualBar.setVisibility(View.GONE);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mScrollListener =  new ScrollListener(mLayoutManager);

        genre = "";

        result = new MaterialDialog.Builder(getActivity())
                .customView(R.layout.finish_notification, false)
                .cancelable(true)
                .positiveText(R.string.agree)
                .build();
        txt_result = result.getView().findViewById(R.id.txt_done);

        material = new MaterialDialog.Builder(getActivity())
                .title(R.string.title_filter)
                .customView(R.layout.filter_dialog_layout, true)
                .positiveText(R.string.agree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        genre = genresSpinner.getSelectedItem().toString();

                        filter_array.clear();

                        if(!filter_date_check.isChecked()){
                            year_start = 1600;
                            year_end = year_end = Calendar.getInstance().get(Calendar.YEAR);
                            filter_array.add(String.valueOf(year_start) + " - " + String.valueOf(year_end));
                        }else{
                            year_start = Integer.valueOf(editText_start.getText().toString());
                            year_end = Integer.valueOf(editText_end.getText().toString());
                            filter_array.add(String.valueOf(year_start) + " - " + String.valueOf(year_end));
                        }

                        if(genresSpinner.getSelectedItemPosition() != 0){
                            if(!genre.equals("")){
                                filter_array.add(genre);
                            }
                        }else{
                            genre = "";
                        }

                        if(filter_tempo_check.isChecked()){
                            mTempoFilter = filter_tempo.getProgress();
                            filter_array.add(String.valueOf(mTempoFilter));
                        }else{
                            mTempoFilter = 0;
                        }

                        filterAdapter.notifyDataSetChanged();

                        if(filterAdapter.getItemCount() == 0){
                            empty_filter_list.setVisibility(View.VISIBLE);
                            filter_list.setVisibility(View.GONE);
                        }else{
                            empty_filter_list.setVisibility(View.GONE);
                            filter_list.setVisibility(View.VISIBLE);
                        }

                        reset();

                        String query = searchView.getQuery().toString();

                        if(query.equals("")){
                            query = "year:" + String.valueOf(year_start) + "-" + String.valueOf(year_end);
                            if(genresSpinner.getSelectedItemPosition() != 0){
                                if(!genre.equals("")){
                                    query += " genre:" + genre;
                                }

                            }
                        }else{
                            query += "* year:" + String.valueOf(year_start) + "-" + String.valueOf(year_end);
                            if(genresSpinner.getSelectedItemPosition() != 0){
                                if(!genre.equals("")){
                                    query += " genre:" + genre;
                                }
                            }
                        }

                        if(!((year_start != year_end) && !query.equals(""))){
                            progressManualBar.setVisibility(View.VISIBLE);
                            empty.setVisibility(View.GONE);
                            mActionListener.search(query,mTempoFilter);
                        }else{
                            if(!genre.equals("")){
                                progressManualBar.setVisibility(View.VISIBLE);
                                empty.setVisibility(View.GONE);
                                mActionListener.search(query,mTempoFilter);
                            }
                        }
                    }
                })
                .negativeText(R.string.disagree)
                .build();

        searchView = getView().findViewById(R.id.search_view);

        LinearLayout filter_layout = getView().findViewById(R.id.filter_view);
        filter_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                material.show();
            }
        });

        filter_list = getView().findViewById(R.id.filter_list);

        filter_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                material.show();
            }
        });

        filter_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                material.show();
                return true;
            }
        });

        final LinearLayoutManager filter_manager = new LinearLayoutManager(getContext());
        filter_manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        filter_list.setLayoutManager(filter_manager);

        filter_array = new ArrayList<String>();
        filterAdapter = new FilterAdapter(filter_array, getContext());
        filter_list.setAdapter(filterAdapter);

        empty_filter_list = getView().findViewById(R.id.empty_filter_list);

        if(filterAdapter.getItemCount() == 0){
            empty_filter_list.setVisibility(View.VISIBLE);
            filter_list.setVisibility(View.GONE);
        }else{
            empty_filter_list.setVisibility(View.GONE);
            filter_list.setVisibility(View.VISIBLE);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(isOnline(getContext())){
                    reset();
                    if(query.equals("")){
                        query = "year:" + String.valueOf(year_start) + "-" + String.valueOf(year_end);
                        if(genresSpinner.getSelectedItemPosition() != 0){
                            if(!genre.equals("")){
                                query += " genre:" + genre;
                            }
                        }
                    }else{
                        query += "* year:" + String.valueOf(year_start) + "-" + String.valueOf(year_end);
                        if(genresSpinner.getSelectedItemPosition() != 0){
                            if(!genre.equals("")){
                                query += " genre:" + genre;
                            }
                        }
                    }

                    if(!((year_start != year_end) && !query.equals(""))){
                        progressManualBar.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.GONE);
                        mActionListener.search(query, mTempoFilter);
                        searchView.clearFocus();
                        return true;
                    }else{
                        if(!genre.equals("")){
                            progressManualBar.setVisibility(View.VISIBLE);
                            empty.setVisibility(View.GONE);
                            mActionListener.search(query,mTempoFilter);
                            return true;
                        }else{
                            return false;
                        }
                    }

                }else{
                    Log.e("Connection", getString(R.string.no_connection));
                    offline = new MaterialDialog.Builder(getContext())
                            .customView(R.layout.error_layout, false)
                            .cancelable(true)
                            .positiveText(R.string.agree)
                            .build();

                    txt_offline = offline.getView().findViewById(R.id.txt_error);
                    txt_offline.setText(R.string.txt_no_connection);
                    offline.show();
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(isOnline(getContext())){
                    if(query.equals("")){
                        reset();

                        if(query.equals("")){
                            query = "year:" + String.valueOf(year_start) + "-" + String.valueOf(year_end);
                            if(genresSpinner.getSelectedItemPosition() != 0){
                                if(!genre.equals("")){
                                    query += " genre:" + genre;
                                }
                            }
                        }else{
                            query += "* year:" + String.valueOf(year_start) + "-" + String.valueOf(year_end);
                            if(genresSpinner.getSelectedItemPosition() != 0){
                                if(!genre.equals("")){
                                    query += " genre:" + genre;
                                }
                            }
                        }


                        if(!((year_start != year_end) && !query.equals(""))){
                            progressManualBar.setVisibility(View.VISIBLE);
                            empty.setVisibility(View.GONE);
                            mActionListener.search(query, mTempoFilter);
                            return true;
                        }else{
                            if(!genre.equals("")){
                                progressManualBar.setVisibility(View.VISIBLE);
                                empty.setVisibility(View.GONE);
                                mActionListener.search(query,mTempoFilter);
                                return true;
                            }else{
                                return false;
                            }
                        }
                    }else{
                        return false;
                    }
                }else{
                    Log.e("Connection", getString(R.string.no_connection));
                    offline = new MaterialDialog.Builder(getContext())
                            .customView(R.layout.error_layout, false)
                            .cancelable(true)
                            .positiveText(R.string.agree)
                            .build();

                    txt_offline = offline.getView().findViewById(R.id.txt_error);
                    txt_offline.setText(R.string.txt_no_connection);
                    offline.show();
                    return false;
                }
            }
        });

        genresSpinner = material.getCustomView().findViewById(R.id.category_filter_spinner);

        genresSpinner.setTitle(getString(R.string.add_genre));
        genresSpinner.setPositiveButton(getString(R.string.accept));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, db.getGenres());

        // Apply the adapter to the spinner
        genresSpinner.setAdapter(adapter);

        genresSpinner.setSelection(-1);

        // Setup search results list
        mAdapter = new SearchResultsAdapter(getActivity(), new SearchResultsAdapter.ItemSelectedListener() {
            @Override
            public void onItemSelected(View itemView, final Song item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.select_manual_mode)
                        .setItems(R.array.manual_modes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    if (null != mListener) {
                                        // Notify the active callbacks interface (the activity, if the
                                        // fragment is attached to one) that an item has been selected.
                                        mListener.onListFragmentInteraction(item,true);
                                    }
                                }else if(which == 1){
                                    selected_song = item;
                                    playlist_list_dialog.show();
                                }else if(which == 2){
                                    selected_song = item;
                                    new_playlist_dialog.show();
                                }
                            }
                        });
                builder.show();
            }
        });

        year_start = 1600;
        year_end = Calendar.getInstance().get(Calendar.YEAR);

        editText_start = material.getCustomView().findViewById(R.id.txt_filter_start_date);
        editText_start.setClickable(true);
        editText_start.setFocusable(true);

        editText_start.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

        editText_end = material.getCustomView().findViewById(R.id.txt_filter_end_date);
        editText_end.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        editText_end.setClickable(true);
        editText_end.setFocusable(true);

        filter_date_check = material.getCustomView().findViewById(R.id.check_filter_date);
        filter_date_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    editText_start.setEnabled(true);
                    editText_end.setEnabled(true);
                }else{
                    editText_start.setEnabled(false);
                    editText_end.setEnabled(false);
                }
            }
        });

        filter_tempo = material.getCustomView().findViewById(R.id.filter_bmp_seekbar);
        filter_tempo.setEnabled(false);

        filter_tempo_check  = material.getCustomView().findViewById(R.id.check_filter_tempo);
        filter_tempo_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    filter_tempo.setEnabled(true);
                }else{
                    filter_tempo.setEnabled(false);
                    filter_tempo.setProgress(20);
                }
            }
        });

        playlist_list_dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.select_playlist)
                .customView(R.layout.playlist_list_short,false)
                .negativeText(R.string.disagree)
                .build();

        playlist_list_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                adapter_playlist_list = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, db.getPlaylistsNames());
                playlist_list_view.setAdapter(adapter_playlist_list);
            }
        });

        new_playlist_dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.new_playlist)
                .customView(R.layout.new_playlist_short, false)
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .autoDismiss(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        EditText edit_name = new_playlist_dialog.getView().findViewById(R.id.new_playlist_name);
                        if(!edit_name.getText().toString().equals("")){

                            int position = db.getNextPosition();

                            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            bitmap = bitmap.copy(bitmap.getConfig(),false);
                            byte[] image = Tools.getByteArray(bitmap);

                            // Obtenemos el nombre de la nueva playlist
                            String name = edit_name.getText().toString();

                            // Obtenemos el tempo seleccionado
                            float tempo = selected_song.getTempo();

                            // Obtenemos las categorías seleccionadas
                            String genre = selected_song.getGenreId();

                            // Obtenemos la duración máxima seleccionada
                            int duration = 0;

                            //Obtenemos la duración de cada canción
                            float song_duration = 0;

                            float acoustiness = 0;

                            float danceability = 0;

                            float energy = 0;

                            float instrumentalness = 0;

                            float liveness = 0;

                            float loudness = 0;

                            int popularity = 0;

                            float speechiness = 0;

                            float valence = 0;

                            // Procedemos a llamar a la API para obtener las canciones
                            UUID newId = UUID.randomUUID();
                            String user = CredentialsHandler.getUserId(getContext());
                            AttPlaylist newPlaylist = new AttPlaylist(newId.toString(), user, position,
                                    name,tempo,duration,song_duration,"","",image,genre, Calendar.getInstance().getTime(),
                                    acoustiness,danceability,energy,instrumentalness,liveness,
                                    loudness,popularity,speechiness,valence);
                            if(db.playlistNameExists(edit_name.getText().toString())){
                                edit_name.setError(getString(R.string.validate_name_exists));
                                edit_name.findFocus();
                            }else{
                                db.createNewPlaylist(newPlaylist,selected_song);
                                new_playlist_dialog.dismiss();
                                txt_result.setText(R.string.playlist_created);
                                result.show();
                            }
                        }else{
                            edit_name.setError(getString(R.string.obligatory_name));
                            edit_name.findFocus();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        new_playlist_dialog.dismiss();
                    }
                })
                .build();

        image = new_playlist_dialog.getView().findViewById(R.id.image_new_manual_playlist);
        Glide.with(getContext())
                .load(R.drawable.baseline_add_photo_alternate_white_48)
                .into(image);

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

        TextView empty_playlist_list = playlist_list_dialog.getCustomView().findViewById(R.id.empty_short_playlist_list_view);

        playlist_list_view = playlist_list_dialog.getCustomView().findViewById(R.id.short_playlist_list);
        adapter_playlist_list = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, db.getPlaylistsNames());
        playlist_list_view.setAdapter(adapter_playlist_list);

        playlist_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String playlist = adapter_playlist_list.getItem(i);
                db.insertSongInPlaylist(selected_song,playlist);
                playlist_list_dialog.dismiss();
                txt_result.setText(getString(R.string.song_added) + " " + playlist);
                result.show();
            }
        });

        if(adapter_playlist_list.getCount() == 0){
            playlist_list_view.setVisibility(View.GONE);
            empty_playlist_list.setVisibility(View.VISIBLE);
        }else{
            playlist_list_view.setVisibility(View.VISIBLE);
            empty_playlist_list.setVisibility(View.GONE);
        }

        resultsList = view.findViewById(R.id.search_results);
        resultsList.setHasFixedSize(true);
        resultsList.setLayoutManager(mLayoutManager);
        resultsList.setAdapter(mAdapter);
        resultsList.addOnScrollListener(mScrollListener);

        String currentQuery = mActionListener.getRecordedQuery(KEY_CURRENT_QUERY);
        if(currentQuery != null){
            if(!currentQuery.equals("")){
                mActionListener.search(currentQuery, mTempoFilter);
                searchView.clearFocus();
            }
        }

        empty = getView().findViewById(R.id.empty_manual_view);

        if(mAdapter.getItemCount() == 0){
            resultsList.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }else{
            resultsList.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }
    }

    public static void stopSearch() {
        reset();
    }

    private static void reset() {
        mScrollListener.reset();
        mAdapter.clearData();

        if(mAdapter.getItemCount() == 0){
            resultsList.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }else{
            resultsList.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Song item, boolean b);
    }

    public static void setMarginBottomList(int mode){
        if(resultsList != null){
            ViewGroup.MarginLayoutParams marginLayoutParams =
                    (ViewGroup.MarginLayoutParams) resultsList.getLayoutParams();
            if(mode == 0){
                marginLayoutParams.setMargins(0, 0, 0, 0);
                resultsList.setLayoutParams(marginLayoutParams);
                resultsList.requestLayout();
            }else if(mode == 1){
                marginLayoutParams.setMargins(0, 0, 0, 200);
                resultsList.setLayoutParams(marginLayoutParams);
                resultsList.requestLayout();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case PICK_IMAGE_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
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
