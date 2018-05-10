package es.app.attune.attune.Database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class AttPlaylist {
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @NotNull
    private String name;
    @NotNull
    private float tempo;
    private int duration;
    private String image;

    @NotNull
    private String genre;

    @NotNull
    private Date creation_date;
    @ToMany(referencedJoinProperty = "id")
    @OrderBy("name ASC")
    private List<Song> songs;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1939289955)
    private transient AttPlaylistDao myDao;
    @Generated(hash = 850861623)
    public AttPlaylist(@NotNull String id, @NotNull String name, float tempo, int duration,
            String image, @NotNull String genre, @NotNull Date creation_date) {
        this.id = id;
        this.name = name;
        this.tempo = tempo;
        this.duration = duration;
        this.image = image;
        this.genre = genre;
        this.creation_date = creation_date;
    }
    @Generated(hash = 965684746)
    public AttPlaylist() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
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
    public int getDuration() {
        return this.duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getGenre() {
        return this.genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public Date getCreation_date() {
        return this.creation_date;
    }
    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }
    public void setSongs(List<Song> list){
        this.songs = list;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 26112372)
    public List<Song> getSongs() {
        if (songs == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SongDao targetDao = daoSession.getSongDao();
            List<Song> songsNew = targetDao._queryAttPlaylist_Songs(id);
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
    @Generated(hash = 948479795)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAttPlaylistDao() : null;
    }


}
