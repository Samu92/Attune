package es.app.attune.attune.Classes;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;

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
import es.app.attune.attune.Database.User;
import es.app.attune.attune.Database.UserDao;
import es.app.attune.attune.Modules.Tools;
import es.app.attune.attune.R;
import kaaes.spotify.webapi.android.models.UserPrivate;

public class DatabaseFunctions {

    private DaoSession session;
    private GenreDao genreDao;
    private AttPlaylistDao attplaylistDao;
    private SongDao songDao;
    private UserDao userDao;

    public DatabaseFunctions(DaoSession session) {
        this.session = session;
        attplaylistDao = this.session.getAttPlaylistDao();
        songDao = this.session.getSongDao();
        genreDao = this.session.getGenreDao();
        userDao = this.session.getUserDao();
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
        result.add(App.getContext().getString(R.string.select_genre));
        for(Genre item : genres){
            result.add(item.getName());
        }
        return result;
    }

    public List<AttPlaylist> getPlaylists(){
        String currentUser = getCurrentUser();
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.UserId.eq(currentUser)).orderAsc(AttPlaylistDao.Properties.Position);
        List<AttPlaylist> list = q.list();
        return list;
    }

    public ArrayList<String> getPlaylistsNames() {
        String currentUser = getCurrentUser();
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
        songDao.insertOrReplaceInTx(item);
        recalculatePlaylistDuration(playlist.getId());
    }

    private String getPlaylistIdByName(String playlist) {
        String currentUser = getCurrentUser();
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.Name.like(playlist),AttPlaylistDao.Properties.UserId.eq(currentUser)).limit(1);
        return q.list().get(0).getId();
    }

    public boolean playlistNameExists(String text) {
        String currentUser = getCurrentUser();
        QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
        q.where(AttPlaylistDao.Properties.Name.like(text.toString()),AttPlaylistDao.Properties.UserId.eq(currentUser));
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

    public void insertCurrentUser(UserPrivate user){
        User temp = new User(user.id,user.display_name,user.email);
        userDao.deleteAll();
        userDao.insertInTx(temp);
    }

    public String getCurrentUser() {
        QueryBuilder<User> q = userDao.queryBuilder();
        q.limit(1);
        if(q.list().size() > 0){
            User user = q.list().get(0);
            return user.getId();
        }else{
            return "";
        }
    }

    public String getPlaylistNameById(String id) {
        if(id != ""){
            QueryBuilder<AttPlaylist> q = attplaylistDao.queryBuilder();
            q.where(AttPlaylistDao.Properties.Position.isNotNull(),AttPlaylistDao.Properties.Id.like(id)).limit(1);
            return q.list().get(0).getName();
        }else{
            return "";
        }
    }

    public void updateSongEffect(Song item) {
        if(item != null){
            songDao.updateInTx(item);
        }
    }
}
