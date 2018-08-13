package es.app.attune.attune.classes;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import es.app.attune.attune.R;
import es.app.attune.attune.database.AttPlaylist;
import es.app.attune.attune.database.AttPlaylistDao;
import es.app.attune.attune.database.DaoSession;
import es.app.attune.attune.database.Genre;
import es.app.attune.attune.database.GenreDao;
import es.app.attune.attune.database.Song;
import es.app.attune.attune.database.SongDao;
import es.app.attune.attune.modules.Tools;

public class DatabaseFunctions {

    private GenreDao genreDao;
    private AttPlaylistDao attplaylistDao;
    private SongDao songDao;

    public DatabaseFunctions(DaoSession session) {
        attplaylistDao = session.getAttPlaylistDao();
        songDao = session.getSongDao();
        genreDao = session.getGenreDao();
    }

    public void insertGenres(List<String> items){
        List<Genre> genres = new ArrayList<>();
        List<String> manual_genres_disallowed = Arrays.asList(App.getContext().getResources().getStringArray(R.array.manual_search_genres_disallowed));
        for(String item : items){
            UUID newId = java.util.UUID.randomUUID();
            Genre genre;
            if (manual_genres_disallowed.contains(item)) {
                genre = new Genre(newId.toString(), item, false);
            } else {
                genre = new Genre(newId.toString(), item, true);
            }
            genres.add(genre);
        }
        genreDao.insertOrReplaceInTx(genres);
    }

    public List<String> getAutomaticGenres() {
        List<Genre> genres = genreDao.loadAll();
        List<String> result = new ArrayList<>();
        result.add(App.getContext().getString(R.string.select_genre));
        for(Genre item : genres){
            result.add(item.getName());
        }
        return result;
    }

    public List<String> getManualGenres() {
        QueryBuilder<Genre> q = genreDao.queryBuilder();
        q.where(GenreDao.Properties.Manual.eq(true));
        List<String> result = new ArrayList<>();
        result.add(App.getContext().getString(R.string.select_genre));
        for (Genre item : q.list()) {
            result.add(item.getName());
        }
        return result;
    }


    public List<AttPlaylist> getPlaylists(){
        String currentUser = CredentialsHandler.getUserId(App.getContext());
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.UserId.eq(currentUser)).orderAsc(AttPlaylistDao.Properties.Position);
        return q.list();
    }

    public ArrayList<String> getPlaylistsNames() {
        String currentUser = CredentialsHandler.getUserId(App.getContext());
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.UserId.eq(currentUser)).orderAsc(AttPlaylistDao.Properties.Position);
        List<AttPlaylist> list = q.list();
        ArrayList<String> temp = new ArrayList<String>();
        for (AttPlaylist item: q.list()) {
            temp.add(item.getName());
        }
        return temp;
    }

    public List<Song> getSongs(String playlistId) {
        QueryBuilder<Song> q = songDao.queryBuilder();
        q.where(SongDao.Properties.IdPlaylist.eq(playlistId)).orderAsc(SongDao.Properties.Position);
        return q.list();
    }

    public void removePlaylist(String id) {
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.Id.eq(id)).limit(1);
        songDao.deleteInTx(q.list().get(0).getSongs());
        attplaylistDao.deleteByKeyInTx(id);
    }

    public void removeSong(String id) {
        QueryBuilder<Song> q = songDao.queryBuilder();
        q.where(SongDao.Properties.Id.eq(id)).limit(1);
        Song temp_song = q.list().get(0);

        QueryBuilder<AttPlaylist> q1 = attplaylistDao.queryBuilder();
        q1.where(AttPlaylistDao.Properties.Id.eq(temp_song.getIdPlaylist())).limit(1);
        AttPlaylist temp_playlist = q1.list().get(0);

        temp_playlist.getSongs().remove(temp_song);
        songDao.deleteInTx(temp_song);
        attplaylistDao.updateInTx(temp_playlist);
    }

    public void insertSongInPlaylist(Song selected_song, String playlist) {
        int position = getSongNextPosition(getPlaylistIdByName(playlist));
        selected_song.setPosition(position);
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.Name.eq(playlist));
        if(q.list().size() > 0){
            AttPlaylist tempPlaylist = q.list().get(0);
            selected_song.setIdPlaylist(tempPlaylist.getId());
            tempPlaylist.getSongs().add(selected_song);
            songDao.insertOrReplaceInTx(selected_song);
            attplaylistDao.updateInTx(tempPlaylist);
            recalculatePlaylistDuration(tempPlaylist.getId());
        }
    }

    public void insertNewPlaylist(AttPlaylist newPlaylist, List<Song> songs) {
        songDao.insertInTx(songs);
        attplaylistDao.insertOrReplaceInTx(newPlaylist);
    }

    public void insertSongsInPlaylist(AttPlaylist playlist, List<Song> songs) {
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.Id.eq(playlist.getId()));
        if(q.list().size() > 0){
            AttPlaylist tempPlaylist = q.list().get(0);
            tempPlaylist.getSongs().addAll(songs);
            songDao.insertOrReplaceInTx(songs);
            attplaylistDao.updateInTx(tempPlaylist);
            recalculatePlaylistDuration(tempPlaylist.getId());
        }
    }

    public void createNewPlaylist(AttPlaylist playlist ,Song item) {
        attplaylistDao.insertOrReplaceInTx(playlist);
        item.setIdPlaylist(playlist.getId());
        item.setPosition(0);
        songDao.insertOrReplaceInTx(item);
        recalculatePlaylistDuration(playlist.getId());
    }

    private String getPlaylistIdByName(String playlist) {
        String currentUser = CredentialsHandler.getUserId(App.getContext());
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.Name.like(playlist),AttPlaylistDao.Properties.UserId.eq(currentUser)).limit(1);
        return q.list().get(0).getId();
    }

    public boolean playlistNameExists(String text) {
        String currentUser = CredentialsHandler.getUserId(App.getContext());
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.Name.like(text), AttPlaylistDao.Properties.UserId.eq(currentUser));
        List<AttPlaylist> playlists = q.list();
        return playlists.size() > 0;
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

    private int getSongNextPosition(String id) {
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

    public AttPlaylist getPlaylistByName(String playlist) {
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.Position.isNotNull(),AttPlaylistDao.Properties.Name.like(playlist)).limit(1);
        return q.list().get(0);
    }

    public void editPlaylist(AttPlaylist selected_playlist, String newPlaylistName, Drawable drawable) {
        selected_playlist.setName(newPlaylistName);
        BitmapDrawable temp_drawable = (BitmapDrawable) drawable;
        Bitmap bitmap = temp_drawable.getBitmap();
        bitmap = bitmap.copy(bitmap.getConfig(),false);
        selected_playlist.setImage(Tools.getByteArray(bitmap));
        attplaylistDao.updateInTx(selected_playlist);
    }

    public void recalculatePlaylistDuration(String idPlaylist) {
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.Id.like(idPlaylist)).limit(1);
        AttPlaylist temp = q.list().get(0);
        List<Song> song_list =  temp.getSongs();
        long newDuration = 0;
        for (Song song : song_list) {
            newDuration += song.getDuration();
        }
        temp.setDuration((int) newDuration);
        attplaylistDao.updateInTx(temp);
    }

    public String getPlaylistNameById(String id) {
        if(!id.equals("")){
            QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
            q.where(AttPlaylistDao.Properties.Position.isNotNull(),AttPlaylistDao.Properties.Id.like(id)).limit(1);
            return q.list().get(0).getName();
        }else{
            return "";
        }
    }

    public AttPlaylist getPlaylistById(String idPlaylist) {
        if(!idPlaylist.equals("")){
            QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
            q.where(AttPlaylistDao.Properties.Position.isNotNull(),AttPlaylistDao.Properties.Id.like(idPlaylist)).limit(1);
            return q.list().get(0);
        }else{
            return null;
        }
    }

    public void updateSongEffect(Song item) {
        if(item != null){
            songDao.updateInTx(item);
        }
    }
}
