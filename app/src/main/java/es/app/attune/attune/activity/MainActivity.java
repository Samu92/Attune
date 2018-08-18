package es.app.attune.attune.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.Spotify;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.app.attune.attune.R;
import es.app.attune.attune.attunePlayer.PlayerService;
import es.app.attune.attune.classes.App;
import es.app.attune.attune.classes.Constants;
import es.app.attune.attune.classes.CredentialsHandler;
import es.app.attune.attune.classes.DatabaseFunctions;
import es.app.attune.attune.classes.SearchFunctions;
import es.app.attune.attune.classes.SearchInterfaces;
import es.app.attune.attune.database.AttPlaylist;
import es.app.attune.attune.database.DaoSession;
import es.app.attune.attune.database.Song;
import es.app.attune.attune.fragments.AutomaticModeTabs;
import es.app.attune.attune.fragments.ManualMode;
import es.app.attune.attune.fragments.NewPlayList;
import es.app.attune.attune.fragments.PlayListFragment;
import es.app.attune.attune.fragments.SongsListFragment;
import es.app.attune.attune.services.RenewService;
import kaaes.spotify.webapi.android.models.UserPrivate;

import static es.app.attune.attune.classes.App.getContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PlayListFragment.OnListFragmentInteractionListener, NewPlayList.OnFragmentInteractionListener,
        SearchInterfaces.ResultPlaylist, SearchInterfaces.ResultGenres, SearchInterfaces.ResultUserData, SearchInterfaces.ResultNewPlaylist,
        SongsListFragment.OnListFragmentInteractionListener, AutomaticModeTabs.OnFragmentInteractionListener, ManualMode.OnListFragmentInteractionListener{

    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";
    private static final int REQUEST_CODE = 1337;
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 10 ;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static ConnectivityManager manager;
    private PlayListFragment playListFragment;
    private SongsListFragment songsListFragment;
    private static AutomaticModeTabs automaticModeTabs;
    private static ManualMode manualMode;
    private DaoSession daoSession;
    private static DatabaseFunctions db;
    private static SearchInterfaces.ActionListener mActionListener;
    static PlayerService mService;
    static RenewService mRenewService;
    boolean mBound = false;
    boolean mRenewBound = false;
    private String token;
    TextView navEmail;
    TextView navUserName;
    CircleImageView navImageView;
    private ImageView image;
    private MaterialDialog edit_playlist_dialog;
    private AttPlaylist selected_playlist;
    private static SlidingUpPanelLayout playerUI;
    private static ImageView play_pause_button;
    private static ImageView play_pause_button_expand;
    private static CircleProgressbar song_cover;
    private static TextView song_playlist_name;
    private static TextView song_title;
    private static TextView song_artist;
    private static LinearLayout playerControlsShort;
    private static CircleImageView song_cover_image;
    private MaterialDialog progress;
    private static MaterialDialog result;
    private static TextView txt_result;
    private static TextView numeric_progress;
    private static TextView numeric_total_progress;
    private static TextView numeric_progress_short;
    private static TextView numeric_total_progress_short;
    private Handler handler;
    private static MaterialDialog error;
    private static TextView txt_error;
    private static ImageView player_state;
    private MaterialDialog offline;
    private TextView txt_offline;
    private ImageView repetition_button_expand;

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        token = CredentialsHandler.getToken(this);
        mActionListener = new SearchFunctions(this, this, this, this, this);
        mActionListener.init(token);

        result = new MaterialDialog.Builder(this)
                .customView(R.layout.finish_notification, false)
                .cancelable(true)
                .positiveText(R.string.agree)
                .build();

        txt_result = result.getView().findViewById(R.id.txt_done);

        progress = new MaterialDialog.Builder(this)
                .customView(R.layout.loading_layout, false)
                .build();
        TextView txt_loading = progress.getView().findViewById(R.id.txt_loading);
        txt_loading.setText(R.string.playlist_loading);

        View headerView = navigationView.getHeaderView(0);
        navEmail = headerView.findViewById(R.id.email);
        navUserName = headerView.findViewById(R.id.username);
        navImageView = headerView.findViewById(R.id.profile_image);

        Glide.with(this)
                .load(R.drawable.default_profile)
                .into(navImageView);

        progress.show();
        mActionListener.getUserData();

        // Inicializamos la sesión de base de datos
        daoSession = ((App) getApplication()).getDaoSession();
        db = new DatabaseFunctions(daoSession);

        // Obtenemos los géneros
        mActionListener.getAvailableGenreSeeds();

        // Inicializamos los fragmentos
        playListFragment = PlayListFragment.newInstance(db);
        automaticModeTabs = AutomaticModeTabs.newInstance(db,mActionListener);
        manualMode = ManualMode.newInstance(db, mActionListener);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        }

        edit_playlist_dialog = new MaterialDialog.Builder(MainActivity.this)
                .title("Edición de playlist")
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
                                PlayListFragment.update();
                            }else{
                                edit_name.setError(getString(R.string.validate_name_exists));
                                edit_name.findFocus();
                            }
                        }else{
                            // Editamos la playlist con nombre que ya tiene
                            db.editPlaylist(selected_playlist, selected_playlist.getName(), image.getDrawable());
                            edit_playlist_dialog.dismiss();
                            PlayListFragment.update();
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
                edit_playlist_dialog.setTitle(selected_playlist.getName());
                Glide.with(MainActivity.this)
                        .load(selected_playlist.getImage())
                        .into(image);
            }
        });

        image = edit_playlist_dialog.getView().findViewById(R.id.image_new_manual_playlist);
        TextView txt_edit_playlist = edit_playlist_dialog.getView().findViewById(R.id.new_playlist_name);

        Glide.with(MainActivity.this)
                .load(R.drawable.ic_image_gray_48dp)
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

        playerUI = (SlidingUpPanelLayout) findViewById(R.id.sliding_panel);
        playerControlsShort = (LinearLayout) findViewById(R.id.playback_controls);
        playerUI.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(newState == SlidingUpPanelLayout.PanelState.DRAGGING){
                    if(previousState == SlidingUpPanelLayout.PanelState.EXPANDED){
                        playerControlsShort.setVisibility(View.VISIBLE);
                    }

                    if(previousState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                        playerControlsShort.setVisibility(View.GONE);
                    }

                    if(previousState == SlidingUpPanelLayout.PanelState.HIDDEN){
                        playerControlsShort.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        playerControlsShort.setClickable(false);

        /* Controles del reproductor */
        play_pause_button = (ImageView) findViewById(R.id.play_pause_button_slide);
        play_pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mService != null){
                    if(mService.playPauseState()){
                        play_pause_button.setImageResource(R.drawable.ic_pause_green_36dp);
                    }else{
                        play_pause_button.setImageResource(R.drawable.ic_play_arrow_green_36dp);
                    }
                }
            }
        });

        ImageView previous_song_button = (ImageView) findViewById(R.id.previous_song);
        previous_song_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mService != null){
                    if(isOnline(getContext())){
                        mService.skipToPreviousSong();
                    }else{
                        txt_offline.setText(R.string.txt_no_connection);
                        offline.show();
                    }
                }
            }
        });

        ImageView next_song_button = (ImageView) findViewById(R.id.next_song);
        next_song_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mService != null){
                    if(isOnline(getContext())){
                        mService.skipToNextSong();
                    }else{
                        txt_offline.setText(R.string.txt_no_connection);
                        offline.show();
                    }
                }
            }
        });

        ImageView previous_song_button_expand = (ImageView) findViewById(R.id.previous_song_expand);
        previous_song_button_expand.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mService != null){
                    if(isOnline(getContext())){
                        mService.skipToPreviousSong();
                    }else{
                        txt_offline.setText(R.string.txt_no_connection);
                        offline.show();
                    }
                }
            }
        });

        ImageView next_song_button_expand = (ImageView) findViewById(R.id.next_song_expand);
        next_song_button_expand.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mService != null){
                    if(isOnline(getContext())){
                        mService.skipToNextSong();
                    }else{
                        txt_offline.setText(R.string.txt_no_connection);
                        offline.show();
                    }
                }
            }
        });

        play_pause_button_expand = (ImageView) findViewById(R.id.play_pause_button_slide_expand);
        play_pause_button_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mService != null){
                    if(mService.playPauseState()){
                        play_pause_button_expand.setImageResource(R.drawable.ic_pause_circle_outline_white_64dp);
                    }else{
                        play_pause_button_expand.setImageResource(R.drawable.ic_play_circle_outline_white_64dp);
                    }
                }
            }
        });

        repetition_button_expand = (ImageView) findViewById(R.id.repetition_mode);
        repetition_button_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mService != null){
                    if(!mService.getRepetitionState()){
                        repetition_button_expand.setImageResource(R.drawable.ic_replay_white_36dp);
                        mService.setRepetitionState(true);
                    }else{
                        repetition_button_expand.setImageResource(R.drawable.ic_replay_gray_36dp);
                        mService.setRepetitionState(false);
                    }
                }
            }
        });

        ImageView slide_button = (ImageView) findViewById(R.id.slide_button);
        slide_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mService != null){
                    playerUI.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
            }
        });
        /* ****************************** */

        song_cover = (CircleProgressbar) findViewById(R.id.circle_progress_player);
        song_cover.setOnProgressbarChangeListener(new CircleProgressbar.OnProgressbarChangeListener() {
            @Override
            public void onProgressChanged(CircleProgressbar circleSeekbar, float progress, boolean fromUser) {
                if(fromUser){
                    if(mService != null){
                        if(isOnline(getContext())){
                            Log.i("SeekToPosition", String.valueOf(progress));
                            mService.seekToPosition((int) progress);
                        }else{
                            txt_offline.setText(R.string.txt_no_connection);
                            offline.show();
                        }
                    }
                }
            }

            @Override
            public void onStartTracking(CircleProgressbar circleSeekbar) {

            }

            @Override
            public void onStopTracking(CircleProgressbar circleSeekbar) {

            }
        });

        song_cover_image = (CircleImageView) findViewById(R.id.image_player_center);
        song_playlist_name = (TextView) findViewById(R.id.song_playlist_player);
        song_title = (TextView) findViewById(R.id.song_title_player);
        song_artist = (TextView) findViewById(R.id.song_artist_player);
        numeric_progress = (TextView) findViewById(R.id.numeric_progress);
        numeric_total_progress = (TextView) findViewById(R.id.total_progress);
        numeric_progress_short = (TextView) findViewById(R.id.numeric_progress_short);
        numeric_total_progress_short = (TextView) findViewById(R.id.total_progress_short);

        numeric_progress_short.setText(String.format("%02d",0) + ":" + String.format("%02d",0));
        numeric_total_progress_short.setText(String.format("%02d",0) + ":" + String.format("%02d",0));

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mService != null) {
                    if(mService.isInitialized()){
                        PlaybackState state = mService.getPlaybackState();
                        if(!mService.currentsSongEmpty()){
                            if (state != null) {
                                MainActivity.sendCurrentPosition(mService.getCurrentSong(), state.positionMs);
                            }
                        }
                    }
                }
                handler.postDelayed(this, 1000);
            }
        }, 1000);

        disableSlidingPanel();

        error = new MaterialDialog.Builder(this)
                .customView(R.layout.error_layout, false)
                .cancelable(true)
                .positiveText(R.string.agree)
                .build();
        txt_error = error.getView().findViewById(R.id.txt_error);

        offline = new MaterialDialog.Builder(this)
                .customView(R.layout.error_layout, false)
                .cancelable(true)
                .positiveText(R.string.agree)
                .build();
        txt_offline = offline.getView().findViewById(R.id.txt_error);

        playerUI.setTouchEnabled(false);

        player_state = (ImageView) findViewById(R.id.player_status);
        if (mService != null) {
            if (mService.isLogged()) {
                player_state.setImageResource(R.drawable.ic_music_note_green_24dp);
            } else {
                player_state.setImageResource(R.drawable.ic_music_note_gray_24dp);
            }
        }

        // AUDIO PLAYER
        // Playback controls configuration:
        // Initialize service player
        Intent player = new Intent(this, PlayerService.class);
        player.setAction(Constants.ACTION.MAIN_ACTION);
        player.putExtra("token", token);
        startService(player);

        // Bind to LocalService
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        Intent renewService = new Intent(this, RenewService.class);
        bindService(renewService, mRenewConnection, Context.BIND_AUTO_CREATE);
    }

    private void initialize() {
        new Handler().post(new Runnable() {
            public void run() {
                if(!isFinishing()){
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack(playListFragment.getClass().getName())
                            .replace(R.id.fragmentView, playListFragment, playListFragment.getClass().getName())
                            .commitAllowingStateLoss();
                }
            }
        });
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
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
                                        .placeholder(R.drawable.ic_image_gray_48dp)
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
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(playerUI.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
            playerUI.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.close_session){
            Spotify.destroyPlayer(this);
            Intent intent = LoginActivity.createIntent(this);
            intent.putExtra("logout",true);
            CredentialsHandler.removeCredentials(this);
            mService.logout();
            unbindService(mConnection);
            unbindService(mRenewConnection);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_auto_mode_playlist) {
            // Si pulsamos el botón mostramos el dialogo de selección de modo
            if (playerUI.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                closePanel();
            }
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(automaticModeTabs.getClass().getName())
                    .replace(R.id.fragmentView, automaticModeTabs, automaticModeTabs.getClass().getName())
                    .commit();
        } else if (id == R.id.nav_manual_mode_playlist) {
            if (playerUI.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                closePanel();
            }
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(manualMode.getClass().getName())
                    .replace(R.id.fragmentView, manualMode, manualMode.getClass().getName())
                    .commit();
        }else if (id == R.id.nav_playlists) {
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(playListFragment.getClass().getName())
                    .replace(R.id.fragmentView, playListFragment, playListFragment.getClass().getName())
                    .commit();
            closePanel();
        } else if (id == R.id.nav_share) {
            String share_text = getString(R.string.share_text);
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, share_text);
            try {
                this.startActivity(whatsappIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Log.e("Share", "Whatsapp have not been installed.");
            }
        }else{
            item.setChecked(false);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        mService.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mRenewService != null){
            mRenewService.renewToken();
        }

        if (CredentialsHandler.getUserProduct(getContext()).equals("premium")) {
            if (mService != null) {
                if (!mService.isLogged()) {
                    player_state = (ImageView) findViewById(R.id.player_status);
                    player_state.setImageResource(R.drawable.ic_music_note_gray_24dp);
                }
            }

            if (mService != null) {
                if (mService.isPlaying()) {
                    showPlayer();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        if (CredentialsHandler.getUserProduct(getContext()).equals("premium")) {
            if (mService != null) {
                if (!mService.isLogged()) {
                    player_state.setImageResource(R.drawable.ic_music_note_gray_24dp);
                }
            }
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
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
            if(mService.getCurrentSong() != null){
                notifyPlayer(mService.getCurrentSong());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    private ServiceConnection mRenewConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            RenewService.RenewBinder binder = (RenewService.RenewBinder) service;
            mRenewService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(final AttPlaylist item, boolean reproducir) {
        songsListFragment = SongsListFragment.newInstance(db,item.getId());

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.select_playlist_option)
                .setItems(R.array.playlist_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if(isOnline(getApplicationContext())){
                                // Reproducción
                                mService.playPlaylist(item);
                                Log.i("PlayPlaylist", "Calling service to reproduce playlist...");
                            }else{
                                Log.e("Connection",getString(R.string.no_connection));
                            }
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
                        } else if (which == 3){
                            // Exportamos la playlist a Spotify
                            if(isOnline(getApplicationContext())){
                                selected_playlist = item;
                                mActionListener.exportToSpotify(CredentialsHandler.getUserId(getApplicationContext()), item);
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
                });
        builder.show();
    }

    @Override
    public void onListFragmentInteraction(final Song item, boolean manual) {
        if(!manual){
            if(isOnline(getApplicationContext())){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.select_song_option)
                        .setItems(R.array.song_options, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    // Reproducir
                                    selected_playlist = db.getPlaylistById(item.getIdPlaylist());
                                    if(selected_playlist != null){
                                        mService.playSongFromPlaylist(selected_playlist, item);
                                    }
                                } else if (which == 1) {
                                    // Asignar efecto
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle(R.string.select_effect_option)
                                            .setItems(R.array.effect_options, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if(which == 0){
                                                        item.setEffect_type(0);
                                                        db.updateSongEffect(item);
                                                        SongsListFragment.updateAdapter();
                                                    } else if (which == 1) {
                                                        // Fade
                                                        item.setEffect_type(1);
                                                        db.updateSongEffect(item);
                                                        SongsListFragment.updateAdapter();
                                                    } else if (which == 2) {
                                                        // Solapamiento
                                                        item.setEffect_type(2);
                                                        db.updateSongEffect(item);
                                                        SongsListFragment.updateAdapter();
                                                    }
                                                }
                                            });
                                    builder.show();
                                }
                            }
                        });
                builder.show();
            }else{
                Log.e("Connection",getString(R.string.no_connection));
                txt_offline.setText(R.string.txt_no_connection);
                offline.show();
            }
        }else{
            if(mService != null){
                mService.playSong(item);
            }
        }
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
        if (!playListFragment.isVisible()) {
            // Si el fragmento no está visible lo mostramos
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(playListFragment.getClass().getName())
                    .replace(R.id.fragmentView, playListFragment, playListFragment.getClass().getName())
                    .commit();
            PlayListFragment.scrollToLastPosition();
        }
    }

    @Override
    public void showError(Throwable message) {
        String msg = message.getMessage();
        if(msg.equals("401 Unauthorized") || msg.equals(("410 The access token expired"))){
            mRenewService.renewToken();
        }else{
            mRenewService.renewToken();
            Log.e("Error",message.getMessage());
            txt_error.setText(R.string.error_ocurred);
            error.show();
        }
    }

    @Override
    public void addDataToSearchList(List<Song> items) {
        manualMode.setAdapter(items);
        ManualMode.stopProgressBar();
    }

    @Override
    public void setUserData(UserPrivate user) {
        progress.dismiss();
        if(user.product.equals("premium")){
            if(user.display_name != null){
                navUserName.setText(user.display_name);
            }else{
                navUserName.setText("Attune");
            }
            navEmail.setText(user.email);
            if (user.images.size() > 0) {
                if (!user.images.get(0).url.equals("")) {
                    Glide.with(this)
                            .load(user.images.get(0).url)
                            .into(navImageView);
                }
            } else {
                Glide.with(this)
                        .load(R.drawable.default_profile)
                        .into(navImageView);
            }
            CredentialsHandler.setUserCredentials(getContext(),user);
            initialize();
        }else{
            if(user.display_name != null){
                navUserName.setText(user.display_name);
            }else{
                navUserName.setText("Attune");
            }
            if (user.images.size() > 0) {
                if (!user.images.get(0).url.equals("")) {
                    Glide.with(this)
                            .load(user.images.get(0).url)
                            .into(navImageView);
                }
            } else {
                Glide.with(this)
                        .load(R.drawable.default_profile)
                        .into(navImageView);
            }

            navEmail.setText(user.email);
            CredentialsHandler.setUserCredentials(getContext(),user);
            initialize();
        }
    }

    public void setUserData() {
        String userDisplayName = CredentialsHandler.getUserDisplayName(getContext());
        String userEmail = CredentialsHandler.getUserEmail(getContext());
        String userImage = CredentialsHandler.getUserImage(getContext());

        if(!userDisplayName.equals("")){
            navUserName.setText(userDisplayName);
        }else{
            navUserName.setText("Attune");
        }
        navEmail.setText(userEmail);
        Glide.with(this)
                .load(userImage)
                .into(navImageView);
        progress.dismiss();
        initialize();
    }


    @Override
    public void dismissProgress() {
        automaticModeTabs.dismissProgress();
    }

    public static void pause(){
        play_pause_button.setImageResource(R.drawable.ic_play_arrow_green_36dp);
        play_pause_button_expand.setImageResource(R.drawable.ic_play_circle_outline_white_64dp);
    }

    public static void play(){
        play_pause_button.setImageResource(R.drawable.ic_pause_green_36dp);
        play_pause_button_expand.setImageResource(R.drawable.ic_pause_circle_outline_white_64dp);
    }

    public static void notifyPlayer(Song currentSong){
        Glide.with(getContext())
                .load(currentSong.getImage())
                .apply(RequestOptions.noAnimation())
                .into(song_cover_image);
        String name = db.getPlaylistNameById(currentSong.getIdPlaylist());
        song_playlist_name.setText(name);
        song_title.setText(currentSong.getName());
        song_artist.setText(currentSong.getArtist());
    }

    public static void closePanel(){
        if (mService != null) {
            if (mService.isPlaying()) {
                playerControlsShort.setVisibility(View.VISIBLE);
                playerUI.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            } else if (playerUI.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                playerControlsShort.setVisibility(View.VISIBLE);
                playerUI.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }
    }

    public static void enableSlidingPanel() {
        playerUI.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    public static void disableSlidingPanel() {
        playerUI.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    public static void sendCurrentPosition(Song mCurrentSong, long positionMs) {
        if(positionMs != 0){
            Log.i("CURRENT POSITION", "TOTAL: " + String.valueOf(mCurrentSong.getDuration()/1000) + " - CURRENT:" +  String.valueOf(positionMs/1000));
            song_cover.setProgress(positionMs/1000);

            long minutes = (positionMs / 1000) / 60;
            long seconds = (positionMs / 1000) % 60;
            numeric_progress.setText(String.format("%02d",minutes) + ":" + String.format("%02d",seconds));

            long minutes_total = (mCurrentSong.getDuration() / 1000) / 60;
            long seconds_total = (mCurrentSong.getDuration() / 1000) % 60;
            numeric_total_progress.setText(String.format("%02d",minutes_total) + ":" + String.format("%02d",seconds_total));


            numeric_progress_short.setText(String.format("%02d",minutes) + ":" + String.format("%02d",seconds));
            numeric_total_progress_short.setText(String.format("%02d",minutes_total) + ":" + String.format("%02d",seconds_total));

            if(songFinishing(positionMs,mCurrentSong.getDuration())){
                initializeEffect(mCurrentSong.getEffect_type());
            }
        }
    }

    public static boolean songFinishing(long positionMs, long totalMs){
        return positionMs >= (totalMs - 20000);
    }

    public static void initializeEffect(int effect_type){
        if(effect_type == 1){
            // Iniciamos efecto fade in - fade out
            mService.doEffect(effect_type);
        }else{
            mService.doEffect(effect_type);
        }
    }

    public static void initializeProgressCircle(Song mCurrentSong){
        song_cover.setMaxProgress(mCurrentSong.getDuration()/1000);
    }

    public static void showPlayer() {
        if(mService != null){
            if(mService.isPlaying()){
                if(playerUI.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED){
                    playerUI.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            }
        }
    }

    public static void createPlaylistSuccess() {
        txt_result.setText(R.string.playlist_exported);
        result.show();
    }

    public static boolean isAuthorized() {
        if(CredentialsHandler.getUserProduct(getContext()).equals("premium")){
            return true;
        }else{
            txt_error.setText(R.string.premium_needed);
            error.show();
            return false;
        }
    }

    public static void initToken(String newToken) {
        mActionListener.init(newToken);
    }

    public static void LostPermissionMessage() {
        txt_error.setText(R.string.lost_permission);
        error.show();
    }

    public static void enablePlayerStatus() {
        player_state.setImageResource(R.drawable.ic_music_note_green_24dp);
    }

    public static void disablePlayerState() {
        player_state.setImageResource(R.drawable.ic_music_note_gray_24dp);
    }

    public static void showNoTracks() {
        txt_error.setText(R.string.no_song_found);
        error.show();
    }
}