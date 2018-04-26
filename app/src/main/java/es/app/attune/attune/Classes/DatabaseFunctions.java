package es.app.attune.attune.Classes;

import java.util.ArrayList;
import java.util.List;

import es.app.attune.attune.Database.DaoSession;
import es.app.attune.attune.Database.Genre;
import es.app.attune.attune.Database.GenreDao;
import es.app.attune.attune.Database.PlaylistDao;
import es.app.attune.attune.Database.SongDao;

public class DatabaseFunctions {

    private DaoSession session;
    private GenreDao genreDao;
    private PlaylistDao playlistDao;
    private SongDao songDao;

    public DatabaseFunctions(DaoSession session) {
        this.session = session;
        playlistDao = this.session.getPlaylistDao();
        songDao = this.session.getSongDao();
        genreDao = this.session.getGenreDao();
    }

    public void insertGenres(List<String> items){
        List<Genre> genres = new ArrayList<>();
        for(String item : items){
            Genre genre = new Genre();
            genre.setName(item);
            genres.add(genre);
        }
        genreDao.insertOrReplaceInTx(genres);
    }

    public List<String> getGenres(){
        List<Genre> genres = genreDao.loadAll();
        List<String> result = new ArrayList<>();
        for(Genre item : genres){
            result.add(item.getName());
        }
        return result;
    }
}
