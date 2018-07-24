package es.app.attune.attune.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.spotify.sdk.android.player.Spotify;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import es.app.attune.attune.AttunePlayer.PlayerService;
import es.app.attune.attune.Classes.App;
import es.app.attune.attune.Classes.DatabaseFunctions;
import es.app.attune.attune.Classes.SearchFunctions;
import es.app.attune.attune.Classes.SearchInterfaces;
import es.app.attune.attune.Database.AttPlaylist;
import es.app.attune.attune.Database.DaoSession;
import es.app.attune.attune.Database.Song;
import es.app.attune.attune.Fragments.ManualMode;
import es.app.attune.attune.Fragments.NewPlayList;
import es.app.attune.attune.Fragments.AutomaticModeTabs;
import es.app.attune.attune.Fragments.PlayListFragment;
import es.app.attune.attune.Fragments.SongsListFragment;
import es.app.attune.attune.Modules.Tools;
import es.app.attune.attune.R;
import kaaes.spotify.webapi.android.models.UserPrivate;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PlayListFragment.OnListFragmentInteractionListener, NewPlayList.OnFragmentInteractionListener,
        SearchInterfaces.ResultPlaylist, SearchInterfaces.ResultGenres, SearchInterfaces.ResultUserData,
        SongsListFragment.OnListFragmentInteractionListener, AutomaticModeTabs.OnFragmentInteractionListener, ManualMode.OnListFragmentInteractionListener{

    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";
    private static final int REQUEST_CODE = 1337;
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 10 ;
    private static final int PICK_IMAGE_REQUEST = 100;

    // Fragments
    private PlayListFragment playListFragment;
    private SongsListFragment songsListFragment;
    private static AutomaticModeTabs automaticModeTabs;
    private static ManualMode manualMode;

    //GreenDao
    private DaoSession daoSession;
    private DatabaseFunctions db;
    private SearchInterfaces.ActionListener mActionListener;

    private MaterialDialog progress;

    PlayerService mService;
    boolean mBound = false;

    // Reproductor
    private ImageButton mPlayPause;
    private TextView mTitle;
    private TextView mSubtitle;
    private ImageView mAlbumArt;
    private ViewGroup mPlaybackControls;

    private MaterialDialog playlist_list_dialog;

    private String token;

    TextView navEmail;
    TextView navUserName;
    CircleImageView navImageView;

    private TextView empty_playlist_list;
    private ListView playlist_list_view;

    private ArrayAdapter<String> adapter_playlist_list;

    private MaterialDialog edit_playlist_dialog;

    private ImageView image;

    private AttPlaylist selected_playlist;

    private TextView txt_loading;

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progress = new MaterialDialog.Builder(MainActivity.this)
                .customView(R.layout.loading_layout, false)
                .build();

        txt_loading = progress.getView().findViewById(R.id.txt_loading);
        txt_loading.setText(R.string.playlist_creating);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        token = intent.getStringExtra(EXTRA_TOKEN);

        mActionListener = new SearchFunctions(this, this, this, this);
        mActionListener.init(token);

        mActionListener.getUserData();

        View headerView = navigationView.getHeaderView(0);
        navEmail = headerView.findViewById(R.id.email);
        navUserName = headerView.findViewById(R.id.username);
        navImageView = headerView.findViewById(R.id.profile_image);

        Glide.with(this).load(R.drawable.default_profile).into(navImageView);

        // Inicializamos la sesión de base de datos
        daoSession = ((App) getApplication()).getDaoSession();
        db = new DatabaseFunctions(daoSession);

        // Obtenemos los géneros
        mActionListener.getAvailableGenreSeeds();

        // Inicializamos los fragmentos
        playListFragment = PlayListFragment.newInstance(db);
        automaticModeTabs = AutomaticModeTabs.newInstance(db);
        manualMode = ManualMode.newInstance(db, mActionListener);

        getSupportFragmentManager().beginTransaction()
                .addToBackStack(playListFragment.getClass().getName())
                .replace(R.id.fragmentView, playListFragment, playListFragment.getClass().getName())
                .commit();

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        }

        FloatingActionButton fab = findViewById(R.id.fabNewPlayList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Si pulsamos el botón mostramos el dialogo de selección de modo
                if(playListFragment.isVisible()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.select_mode)
                            .setItems(R.array.modes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which == 0){
                                        getSupportFragmentManager().beginTransaction()
                                                .addToBackStack(automaticModeTabs.getClass().getName())
                                                .replace(R.id.fragmentView, automaticModeTabs, automaticModeTabs.getClass().getName())
                                                .commit();
                                    }else if(which == 1){
                                        getSupportFragmentManager().beginTransaction()
                                                .addToBackStack(manualMode.getClass().getName())
                                                .replace(R.id.fragmentView, manualMode, manualMode.getClass().getName())
                                                .commit();
                                    }
                                }
                            });
                    builder.show();
                }else if(automaticModeTabs.isVisible()){
                    // El formulario es correcto por lo que obtenemos los parámetros y empezamos el proceso
                    // Obtenemos la imagen de la playlist

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(R.string.select_playlist_option)
                                .setItems(R.array.create_playlist_options, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which == 0){
                                            if(automaticModeTabs.validarFormulario(0)){
                                                progress.show();

                                                //start a new thread to process job
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        int position = db.getNextPosition();

                                                        byte[] image = Tools.getByteArray(automaticModeTabs.getImage());

                                                        // Obtenemos el nombre de la nueva playlist
                                                        String name = automaticModeTabs.getName();

                                                        // Obtenemos el tempo seleccionado
                                                        int tempo = automaticModeTabs.getTempo();

                                                        // Obtenemos las categorías seleccionadas
                                                        String genre = automaticModeTabs.getCategory();

                                                        // Obtenemos la duración máxima seleccionada
                                                        int duration = automaticModeTabs.getDuration();

                                                        //Obtenemos la duración de cada canción
                                                        float song_duration = automaticModeTabs.getSongDuration();

                                                        float acoustiness = automaticModeTabs.getAcousticness();

                                                        float danceability = automaticModeTabs.getDanceability();

                                                        float energy = automaticModeTabs.getEnergy();

                                                        float instrumentalness = automaticModeTabs.getInstrumentalness();

                                                        float liveness = automaticModeTabs.getLiveness();

                                                        float loudness = automaticModeTabs.getLoudness();

                                                        int popularity = automaticModeTabs.getPopularity();

                                                        float speechiness = automaticModeTabs.getSpeechiness();

                                                        float valence = automaticModeTabs.getValence();

                                                        String date_start = automaticModeTabs.getYearStart();

                                                        String date_end = automaticModeTabs.getYearEnd();

                                                        // Procedemos a llamar a la API para obtener las canciones
                                                        UUID newId = java.util.UUID.randomUUID();
                                                        AttPlaylist newPlaylist = new AttPlaylist(newId.toString(), position,
                                                                name,tempo,duration,song_duration, date_start, date_end, image,genre, Calendar.getInstance().getTime(),
                                                                acoustiness,danceability,energy,instrumentalness,liveness,
                                                                loudness,popularity,speechiness,valence);
                                                        mActionListener.searchRecomendations(newPlaylist,0);
                                                    }
                                                }).start();
                                            }
                                        }else if(which == 1){
                                            if(automaticModeTabs.validarFormulario(1)){
                                                playlist_list_dialog.show();
                                            }
                                        }
                                    }
                                });
                        builder.show();
                }
            }
        });

        playlist_list_dialog = new MaterialDialog.Builder(MainActivity.this)
                .title(R.string.select_playlist)
                .customView(R.layout.playlist_list_short,false)
                .negativeText(R.string.disagree)
                .build();

        playlist_list_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                adapter_playlist_list = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, db.getPlaylistsNames());
                playlist_list_view.setAdapter(adapter_playlist_list);
                adapter_playlist_list.notifyDataSetChanged();
            }
        });

        empty_playlist_list = playlist_list_dialog.getCustomView().findViewById(R.id.empty_short_playlist_list_view);

        playlist_list_view = playlist_list_dialog.getCustomView().findViewById(R.id.short_playlist_list);
        adapter_playlist_list = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, db.getPlaylistsNames());
        playlist_list_view.setAdapter(adapter_playlist_list);

        playlist_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String playlist = adapter_playlist_list.getItem(i);
                if(db.playlistNameExists(playlist)){
                    AttPlaylist selected_playlist = db.getPlaylistByName(playlist);
                    selected_playlist.setDuration(automaticModeTabs.getDuration());
                    mActionListener.searchRecomendations(selected_playlist,1);
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

        mPlaybackControls = findViewById(R.id.playback_controls);
        mPlayPause = findViewById(R.id.play_pause);
        mPlayPause.setEnabled(true);
        mPlayPause.setOnClickListener(mPlaybackButtonListener);

        mTitle = findViewById(R.id.title);
        mSubtitle = findViewById(R.id.artist);
        mAlbumArt = findViewById(R.id.album_art);

        edit_playlist_dialog = new MaterialDialog.Builder(MainActivity.this)
                .title("Nueva playlist")
                .customView(R.layout.new_playlist_short, false)
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .autoDismiss(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText edit_name = edit_playlist_dialog.getView().findViewById(R.id.new_playlist_name);
                        if(!edit_name.getText().toString().equals("")){
                            if(!db.playlistNameExists(edit_name.getText().toString())){
                                // Editamos la playlist
                                db.editPlaylist(selected_playlist,edit_name.getText().toString(),image.getDrawable());
                                edit_playlist_dialog.dismiss();
                                playListFragment.update();
                            }else{
                                edit_name.setError(getString(R.string.validate_name_exists));
                                edit_name.findFocus();
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
                        edit_playlist_dialog.dismiss();
                    }
                })
                .build();

        edit_playlist_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Glide.with(MainActivity.this)
                        .load(selected_playlist.getImage())
                        .into(image);
            }
        });

        image = edit_playlist_dialog.getView().findViewById(R.id.image_new_manual_playlist);
        Glide.with(MainActivity.this)
                .load(R.drawable.baseline_add_photo_alternate_white_48)
                .into(image);

        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)),
                        PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case PICK_IMAGE_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();

                    // method 1
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(), selectedImage);
                        Glide.with(MainActivity.this)
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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        // AUDIO PLAYER
        // Playback controls configuration:
        // Initialize service player
        Intent player = new Intent(getApplicationContext(), PlayerService.class);
        player.putExtra("token",token);
        startService(player);

        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            if(playListFragment.isVisible()){
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }else{
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.close_session){
            Spotify.destroyPlayer(this);
            Intent intent = LoginActivity.createIntent(this);
            intent.putExtra("logout",true);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_playlists) {
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(playListFragment.getClass().getName())
                    .replace(R.id.fragmentView, playListFragment, playListFragment.getClass().getName())
                    .commit();
        } else if (id == R.id.nav_share) {

        }else{
            item.setChecked(false);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        if(mBound){
            unbindService(mConnection);
            mBound = false;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        // Bind to LocalService
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        super.onStart();
    }

    @Override
    protected void onStop() {
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        super.onStop();
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(final AttPlaylist item, boolean reproducir) {
        if (reproducir) {

        }else {
            // Hemos recibido un click en una de las playlist
            songsListFragment = SongsListFragment.newInstance(db, item.getId());
            // Mostramos la lista de canciones
            if (!songsListFragment.isVisible()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.select_playlist_option)
                        .setItems(R.array.playlist_options, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    // Reproducción

                                } else if (which == 1) {
                                    // Si el fragmento no está visible lo mostramos
                                    getSupportFragmentManager().beginTransaction()
                                            .addToBackStack(songsListFragment.getClass().getName())
                                            .replace(R.id.fragmentView, songsListFragment, songsListFragment.getClass().getName())
                                            .commit();
                                } else if (which == 2) {
                                    // Editamos la playlist
                                    selected_playlist = item;
                                    edit_playlist_dialog.show();
                                }
                            }
                        });
                builder.show();
            }
        }
    }

    @Override
    public void onListFragmentInteraction(Song item) {
        mService.playSong(item.getUrlSpotify());
    }


    @Override
    public void reset() {

    }

    @Override
    public void addDataGenres(List<String> items) {
        // Almacenamos en base de datos la lista de categorías
        db.insertGenres(items);
    }

    @Override
    public void showListPlaylist() {
        progress.dismiss();
        if (!playListFragment.isVisible()) {
            // Si el fragmento no está visible lo mostramos
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(playListFragment.getClass().getName())
                    .replace(R.id.fragmentView, playListFragment, playListFragment.getClass().getName())
                    .commit();
        }
    }

    @Override
    public void showError(String message) {
        progress.dismiss();
    }

    @Override
    public void addDataToSearchList(List<Song> items) {
        manualMode.setAdapter(items);
    }

    @Override
    public void setUserData(UserPrivate user) {
        if(user.product.equals("premium")){
            navUserName.setText(user.id);
            navEmail.setText(user.email);
            Glide.with(this)
                    .load(user.images.get(0).url)
                    .into(navImageView);
        }else{
            navUserName.setText("Usuario de Atunne");
            navEmail.setText("anónimo@attune.es");
        }
    }

    private final View.OnClickListener mPlaybackButtonListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
    };
}