package es.app.attune.attune.Database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class Song {
    @Id(autoincrement = true)
    private long id;

    @ToOne(joinProperty = "id")
    @NotNull
    private Playlist playlist;
    @NotNull
    private String idSpotify;
    private long genreId;
    @NotNull
    private String name;
    @NotNull
    private int duration;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1369727947)
    private transient SongDao myDao;

    @Generated(hash = 1010486704)
    private transient Long playlist__resolvedKey;
    @Generated(hash = 1569558045)
    public Song(long id, @NotNull String idSpotify, long genreId, @NotNull String name, int duration) {
        this.id = id;
        this.idSpotify = idSpotify;
        this.genreId = genreId;
        this.name = name;
        this.duration = duration;
    }
    @Generated(hash = 87031450)
    public Song() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getIdSpotify() {
        return this.idSpotify;
    }
    public void setIdSpotify(String idSpotify) {
        this.idSpotify = idSpotify;
    }
    public long getGenreId() {
        return this.genreId;
    }
    public void setGenreId(long genreId) {
        this.genreId = genreId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getDuration() {
        return this.duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2118004833)
    public Playlist getPlaylist() {
        long __key = this.id;
        if (playlist__resolvedKey == null || !playlist__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PlaylistDao targetDao = daoSession.getPlaylistDao();
            Playlist playlistNew = targetDao.load(__key);
            synchronized (this) {
                playlist = playlistNew;
                playlist__resolvedKey = __key;
            }
        }
        return playlist;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 408682790)
    public void setPlaylist(@NotNull Playlist playlist) {
        if (playlist == null) {
            throw new DaoException(
                    "To-one property 'id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.playlist = playlist;
            id = playlist.getId();
            playlist__resolvedKey = id;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 767980484)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSongDao() : null;
    }
}
