package es.app.attune.attune.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import es.app.attune.attune.Classes.App;
import es.app.attune.attune.Classes.DatabaseFunctions;
import es.app.attune.attune.Classes.SpotifyPlayer;
import es.app.attune.attune.Classes.Progress;
import es.app.attune.attune.Classes.SearchInterfaces;
import es.app.attune.attune.Classes.SearchFunctions;
import es.app.attune.attune.Database.AttPlaylist;
import es.app.attune.attune.Database.DaoSession;
import es.app.attune.attune.Database.Song;
import es.app.attune.attune.Fragments.NewPlayList;
import es.app.attune.attune.Fragments.PlayListFragment;
import es.app.attune.attune.Fragments.SongsListFragment;
import es.app.attune.attune.Modules.Tools;
import es.app.attune.attune.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlayListFragment.OnListFragmentInteractionListener, NewPlayList.OnFragmentInteractionListener, SearchInterfaces.ResultPlaylist, SearchInterfaces.ResultGenres, SongsListFragment.OnListFragmentInteractionListener {

    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";

    private static final int REQUEST_CODE = 1337;
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 10 ;

    // Fragments
    private PlayListFragment playListFragment;
    private NewPlayList newPlayListFragment;
    private SongsListFragment songsListFragment;

    //GreenDao
    private DaoSession daoSession;
    private DatabaseFunctions db;
    private SearchInterfaces.ActionListener mActionListener;

    private Progress progress;

    private SpotifyPlayer player;

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        player = new SpotifyPlayer();

        progress = new Progress(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        String token = intent.getStringExtra(EXTRA_TOKEN);

        mActionListener = new SearchFunctions(this, this, this);
        mActionListener.init(token);

        // Inicializamos la sesión de base de datos
        daoSession = ((App) getApplication()).getDaoSession();
        db = new DatabaseFunctions(daoSession);

        // Obtenemos los géneros
        mActionListener.getAvailableGenreSeeds();

        // Inicializamos los fragmentos
        newPlayListFragment = NewPlayList.newInstance(db);
        playListFragment = PlayListFragment.newInstance(db);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentView, playListFragment, playListFragment.getClass().getName())
                .commit();

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        }

        // Datos de la sesion de spotify
        //mActionListener.

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabNewPlayList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Begin the transaction
                if (!newPlayListFragment.isVisible()) {
                    // Si el fragmento no está visible lo mostramos
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentView, newPlayListFragment, newPlayListFragment.getClass().getName())
                            .commit();
                } else {
                    // En caso de que esté visible comenzamos el proceso de creación de la playlist
                    if (newPlayListFragment.ValidarFormulario()) {
                        // El formulario es correcto por lo que obtenemos los parámetros y empezamos el proceso
                        // Obtenemos la imagen de la playlist
                        progress.setCancelable(false);
                        progress.show();

                        //start a new thread to process job
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                byte[] image = Tools.getByteArray(newPlayListFragment.getImage());

                                // Obtenemos el nombre de la nueva playlist
                                String name = newPlayListFragment.getName();

                                // Obtenemos el tempo seleccionado
                                int tempo = newPlayListFragment.getTempo();

                                // Obtenemos las categorías seleccionadas
                                String genre = newPlayListFragment.getCategory();

                                // Obtenemos la duración máxima seleccionada
                                int duration = newPlayListFragment.getDuration();

                                // Procedemos a llamar a la API para obtener las canciones
                                //Playlist newPlaylist = new Playlist(java.util.UUID.randomUUID().node(),name,tempo,duration, Tools.BitMapToString(image), Calendar.getInstance().getTime());
                                UUID newId = java.util.UUID.randomUUID();
                                AttPlaylist newPlaylist = new AttPlaylist(newId.toString(),name,tempo,duration,image,genre,Calendar.getInstance().getTime());
                                mActionListener.searchRecomendations(newPlaylist);
                            }
                        }).start();

                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_playlists) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentView, playListFragment, playListFragment.getClass().getName())
                    .commit();
        } else if (id == R.id.nav_share) {

        }else{
            item.setChecked(false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        mActionListener.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActionListener.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActionListener.resume();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(AttPlaylist item) {

        // Hemos recibido un click en una de las playlist
        //Toast.makeText(this,item.getName(),Toast.LENGTH_SHORT).show();
        songsListFragment = SongsListFragment.newInstance(db,item.getId());
        // Mostramos la lista de canciones
        if (!songsListFragment.isVisible()) {
            // Si el fragmento no está visible lo mostramos
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentView, songsListFragment, songsListFragment.getClass().getName())
                    .commit();
        }
    }

    @Override
    public void onListFragmentInteraction(Song item) {
        mActionListener.selectTrack(item);
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
                    .replace(R.id.fragmentView, playListFragment, playListFragment.getClass().getName())
                    .commit();
        }
    }

    @Override
    public void showError(String message) {
        progress.dismiss();
    }


}