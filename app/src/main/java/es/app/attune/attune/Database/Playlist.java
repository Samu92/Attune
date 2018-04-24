package es.app.attune.attune.Database;

import android.graphics.Bitmap;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.DaoException;

// This annotation defines this class as table to green dao. Next we need to
// give attributes to this class which will work like columns of the table

@Entity
public class Playlist {
    // First thing would be a unique key which could be auto increment.
    @Id(autoincrement = true)
    private long id;

    @NotNull
    private String name;
    @NotNull
    private float tempo;
    private float duration;
    private String image;
    @NotNull
    private Date creation_date;
    @ToMany(referencedJoinProperty = "id")
    @OrderBy("name ASC")
    private List<Song> songs;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1436610739)
    private transient PlaylistDao myDao;


    @Generated(hash = 432695716)
    public Playlist(long id, @NotNull String name, float tempo, float duration,
            String image, @NotNull Date creation_date) {
        this.id = id;
        this.name = name;
        this.tempo = tempo;
        this.duration = duration;
        this.image = image;
        this.creation_date = creation_date;
    }
    @Generated(hash = 1160175056)
    public Playlist() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long Id) {
        this.id = Id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getTempo() {
        return this.tempo;
    }
    public void setTempo(float tempo) {
        this.tempo = tempo;
    }
    public float getDuration() {
        return this.duration;
    }
    public void setDuration(float duration) {
        this.duration = duration;
    }
    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public Date getCreation_date() {
        return this.creation_date;
    }
    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2141196397)
    public List<Song> getSongs() {
        if (songs == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SongDao targetDao = daoSession.getSongDao();
            List<Song> songsNew = targetDao._queryPlaylist_Songs(id);
            synchronized (this) {
                if (songs == null) {
                    songs = songsNew;
                }
            }
        }
        return songs;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 432021166)
    public synchronized void resetSongs() {
        songs = null;
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
    @Generated(hash = 226526955)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPlaylistDao() : null;
    }

}
