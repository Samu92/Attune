package es.app.attune.attune.Database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class Song {
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @NotNull
    private String idPlaylist;

    @ToOne(joinProperty = "idPlaylist")
    @NotNull
    private AttPlaylist playlist;
    @NotNull
    private String idSpotify;
    @NotNull
    private String genreId;
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
    @Generated(hash = 2014409455)
    public Song(@NotNull String id, @NotNull String idPlaylist, @NotNull String idSpotify,
            @NotNull String genreId, @NotNull String name, int duration) {
        this.id = id;
        this.idPlaylist = idPlaylist;
        this.idSpotify = idSpotify;
        this.genreId = genreId;
        this.name = name;
        this.duration = duration;
    }
    @Generated(hash = 87031450)
    public Song() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIdSpotify() {
        return this.idSpotify;
    }
    public void setIdSpotify(String idSpotify) {
        this.idSpotify = idSpotify;
    }
    public String getGenreId() {
        return this.genreId;
    }
    public void setGenreId(String genreId) {
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
    @Generated(hash = 358583121)
    private transient String playlist__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 126767090)
    public AttPlaylist getPlaylist() {
        String __key = this.idPlaylist;
        if (playlist__resolvedKey == null || playlist__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AttPlaylistDao targetDao = daoSession.getAttPlaylistDao();
            AttPlaylist playlistNew = targetDao.load(__key);
            synchronized (this) {
                playlist = playlistNew;
                playlist__resolvedKey = __key;
            }
        }
        return playlist;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1321738846)
    public void setPlaylist(@NotNull AttPlaylist playlist) {
        if (playlist == null) {
            throw new DaoException(
                    "To-one property 'idPlaylist' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.playlist = playlist;
            idPlaylist = playlist.getId();
            playlist__resolvedKey = idPlaylist;
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
    public String getIdPlaylist() {
        return this.idPlaylist;
    }
    public void setIdPlaylist(String idPlaylist) {
        this.idPlaylist = idPlaylist;
    }


 
}
