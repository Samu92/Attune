package es.app.attune.attune.Classes;

import android.text.Editable;

import org.greenrobot.greendao.query.QueryBuilder;

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
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.orderAsc(AttPlaylistDao.Properties.Position);
        List<AttPlaylist> list = q.list();
        return list;
    }

    public ArrayList<String> getPlaylistsNames() {
        ArrayList<String> temp = new ArrayList<String>();
        List<AttPlaylist> tempP =  attplaylistDao.loadAll();
        for (AttPlaylist playlist: tempP) {
            temp.add(playlist.getName());
        }
        return temp;
    }

    public List<Song> getSongs(String playlistId) {
        QueryBuilder<Song> q = songDao.queryBuilder();
        q.where(SongDao.Properties.IdPlaylist.eq(playlistId)).orderAsc(SongDao.Properties.Position);
        //List<Song> songs = new ArrayList<>();
        //songs = songDao._queryAttPlaylist_Songs(playlistId);
        return q.list();
    }

    public void insertSong(Song song) {
        songDao.insertInTx(song);
    }

    public void removePlaylist(String id) {
        attplaylistDao.deleteByKeyInTx(id);
    }

    public void removeSong(String id) {
        songDao.deleteByKeyInTx(id);
    }

    public void insertSongInPlaylist(Song selected_song, String playlist) {
        int position = getSongNextPosition(getPlaylistIdByName(playlist));
        selected_song.setPosition(position);
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.Name.eq(playlist));
        if(q.list().size() > 0){
            AttPlaylist tempPlaylist = q.list().get(0);
            selected_song.setIdPlaylist(tempPlaylist.getId());
            songDao.insertOrReplaceInTx(selected_song);
        }
    }


    public void createNewPlaylist(AttPlaylist playlist ,Song item) {
        attplaylistDao.insertOrReplaceInTx(playlist);
        item.setIdPlaylist(playlist.getId());
        songDao.insertOrReplaceInTx(item);
    }

    private String getPlaylistIdByName(String playlist) {
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.Name.like(playlist)).limit(1);
        return q.list().get(0).getId();
    }

    public boolean playlistNameExists(Editable text) {
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.Name.like(text.toString()));
        List<AttPlaylist> playlists = q.list();
        if(playlists.size() > 0){
            return true;
        }else{
            return false;
        }
    }

    public int getNextPosition() {
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.Position.isNotNull()).orderDesc(AttPlaylistDao.Properties.Position).limit(1);
        if(q.list().size() > 0){
            return q.list().get(0).getPosition() + 1;
        }else{
            return 0;
        }
    }

    public int getSongNextPosition(String id) {
        QueryBuilder<Song> q = songDao.queryBuilder();
        q.where(SongDao.Properties.Position.isNotNull(),SongDao.Properties.IdPlaylist.eq(id)).orderDesc(SongDao.Properties.Position).limit(1);
        if(q.list().size() > 0){
            return q.list().get(0).getPosition() + 1;
        }else{
            return 0;
        }
    }

    public void changePosition(AttPlaylist itemA, AttPlaylist itemB, int newIndex, int oldIndex) {
        itemA.setPosition(newIndex);
        itemB.setPosition(oldIndex);
        attplaylistDao.insertOrReplaceInTx(itemA,itemB);
    }

    public void changeSongPosition(Song itemA, Song itemB, int newIndex, int oldIndex) {
        itemA.setPosition(newIndex);
        itemB.setPosition(oldIndex);
        songDao.insertOrReplaceInTx(itemA,itemB);
    }

}
