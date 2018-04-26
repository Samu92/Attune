package es.app.attune.attune.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import es.app.attune.attune.Classes.App;
import es.app.attune.attune.Classes.DatabaseFunctions;
import es.app.attune.attune.Classes.SearchInterfaces;
import es.app.attune.attune.Classes.SearchFunctions;
import es.app.attune.attune.Database.DaoSession;
import es.app.attune.attune.Fragments.NewPlayList;
import es.app.attune.attune.Fragments.PlayListFragment;
import es.app.attune.attune.Fragments.dummy.DummyContent;
import es.app.attune.attune.R;
import kaaes.spotify.webapi.android.models.Track;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlayListFragment.OnListFragmentInteractionListener, NewPlayList.OnFragmentInteractionListener, SearchInterfaces.ResultPlaylist, SearchInterfaces.ResultGenres {

    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";

    private static final int REQUEST_CODE = 1337;

    // Fragments
    private PlayListFragment playListFragment;
    private NewPlayList newPlayListFragment;

    //GreenDao
    private DaoSession daoSession;
    private DatabaseFunctions db;
    private SearchInterfaces.ActionListener mActionListener;

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        playListFragment = PlayListFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentView, playListFragment, playListFragment.getClass().getName())
                .commit();

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
                        Bitmap image = newPlayListFragment.getImage();

                        // Obtenemos el nombre de la nueva playlist
                        String name = newPlayListFragment.getName();

                        // Obtenemos el tempo seleccionado
                        int tempo = newPlayListFragment.getTempo();

                        // Obtenemos las categorías seleccionadas
                        String genre = newPlayListFragment.getCategory();

                        // Procedemos a llamar a la API para obtener las canciones
                        mActionListener.searchRecomendations(tempo, genre);

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
            super.onBackPressed();
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

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

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
    public void addDataPlaylist(List<Track> items) {
        this.getResources().getStringArray(R.array.Categories);
    }
}