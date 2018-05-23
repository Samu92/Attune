package es.app.attune.attune.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.app.attune.attune.Database.AttPlaylist;
import es.app.attune.attune.Database.AttPlaylistDao;
import es.app.attune.attune.Database.DaoSession;
import es.app.attune.attune.Database.Genre;
import es.app.attune.attune.Database.GenreDao;
import es.app.attune.attune.Database.Song;
import es.app.attune.attune.Database.SongDao;

public class DatabaseFunctions {

    private DaoSession session;
    private GenreDao genreDao;
    private AttPlaylistDao attplaylistDao;
    private SongDao songDao;

    public DatabaseFunctions(DaoSession session) {
        this.session = session;
        attplaylistDao = this.session.getAttPlaylistDao();
        songDao = this.session.getSongDao();
        genreDao = this.session.getGenreDao();
    }

    public void insertGenres(List<String> items){
        List<Genre> genres = new ArrayList<>();
        for(String item : items){
            UUID newId = java.util.UUID.randomUUID();
            Genre genre = new Genre(newId.toString(),item);
            genres.add(genre);
        }
        genreDao.insertOrReplaceInTx(genres);
    }

    public List<String> getGenres(){
        List<Genre> genres = genreDao.loadAll();
        List<String> result = new ArrayList<>();
        result.add("Ninguna categoría seleccionada");
        for(Genre item : genres){
            result.add(item.getName());
        }
        return result;
    }

    public void insertNewPlaylist(AttPlaylist newPlaylist) {
        attplaylistDao.insertOrReplaceInTx(newPlaylist);
    }

    public List<AttPlaylist> getPlaylists(){
        return attplaylistDao.loadAll();
    }

    public void insertSong(Song song) {
        songDao.insertInTx(song);
    }

    public void removePlaylist(String id) {
        attplaylistDao.deleteByKey(id);
    }

    public void removeSong(String id) {
        songDao.deleteByKey(id);
    }

    public List<Song> getSongs(String playlistId) {
        List<Song> songs = new ArrayList<>();
        songs = songDao._queryAttPlaylist_Songs(playlistId);
        return songs;
    }
}
