package es.app.attune.attune.Database;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SONG".
*/
public class SongDao extends AbstractDao<Song, String> {

    public static final String TABLENAME = "SONG";

    /**
     * Properties of entity Song.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property IdPlaylist = new Property(1, String.class, "idPlaylist", false, "ID_PLAYLIST");
        public final static Property IdSpotify = new Property(2, String.class, "idSpotify", false, "ID_SPOTIFY");
        public final static Property UrlSpotify = new Property(3, String.class, "urlSpotify", false, "URL_SPOTIFY");
        public final static Property GenreId = new Property(4, String.class, "genreId", false, "GENRE_ID");
        public final static Property Name = new Property(5, String.class, "name", false, "NAME");
        public final static Property Duration = new Property(6, long.class, "duration", false, "DURATION");
        public final static Property Tempo = new Property(7, float.class, "tempo", false, "TEMPO");
        public final static Property Artist = new Property(8, String.class, "artist", false, "ARTIST");
        public final static Property Image = new Property(9, String.class, "image", false, "IMAGE");
        public final static Property PreviewUrl = new Property(10, String.class, "previewUrl", false, "PREVIEW_URL");
        public final static Property Acousticness = new Property(11, float.class, "acousticness", false, "ACOUSTICNESS");
        public final static Property Danceability = new Property(12, float.class, "danceability", false, "DANCEABILITY");
        public final static Property Energy = new Property(13, float.class, "energy", false, "ENERGY");
        public final static Property Instrumentalness = new Property(14, float.class, "instrumentalness", false, "INSTRUMENTALNESS");
        public final static Property Liveness = new Property(15, float.class, "liveness", false, "LIVENESS");
        public final static Property Loudness = new Property(16, float.class, "loudness", false, "LOUDNESS");
        public final static Property Popularity = new Property(17, int.class, "popularity", false, "POPULARITY");
        public final static Property Speechiness = new Property(18, float.class, "speechiness", false, "SPEECHINESS");
        public final static Property Valence = new Property(19, float.class, "valence", false, "VALENCE");
        public final static Property Date = new Property(20, String.class, "date", false, "DATE");
    }

    private Query<Song> attPlaylist_SongsQuery;

    public SongDao(DaoConfig config) {
        super(config);
    }
    
    public SongDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SONG\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"ID_PLAYLIST\" TEXT NOT NULL ," + // 1: idPlaylist
                "\"ID_SPOTIFY\" TEXT NOT NULL ," + // 2: idSpotify
                "\"URL_SPOTIFY\" TEXT NOT NULL ," + // 3: urlSpotify
                "\"GENRE_ID\" TEXT NOT NULL ," + // 4: genreId
                "\"NAME\" TEXT NOT NULL ," + // 5: name
                "\"DURATION\" INTEGER NOT NULL ," + // 6: duration
                "\"TEMPO\" REAL NOT NULL ," + // 7: tempo
                "\"ARTIST\" TEXT NOT NULL ," + // 8: artist
                "\"IMAGE\" TEXT NOT NULL ," + // 9: image
                "\"PREVIEW_URL\" TEXT," + // 10: previewUrl
                "\"ACOUSTICNESS\" REAL NOT NULL ," + // 11: acousticness
                "\"DANCEABILITY\" REAL NOT NULL ," + // 12: danceability
                "\"ENERGY\" REAL NOT NULL ," + // 13: energy
                "\"INSTRUMENTALNESS\" REAL NOT NULL ," + // 14: instrumentalness
                "\"LIVENESS\" REAL NOT NULL ," + // 15: liveness
                "\"LOUDNESS\" REAL NOT NULL ," + // 16: loudness
                "\"POPULARITY\" INTEGER NOT NULL ," + // 17: popularity
                "\"SPEECHINESS\" REAL NOT NULL ," + // 18: speechiness
                "\"VALENCE\" REAL NOT NULL ," + // 19: valence
                "\"DATE\" TEXT);"); // 20: date
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_SONG_ID ON \"SONG\"" +
                " (\"ID\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SONG\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Song entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getId());
        stmt.bindString(2, entity.getIdPlaylist());
        stmt.bindString(3, entity.getIdSpotify());
        stmt.bindString(4, entity.getUrlSpotify());
        stmt.bindString(5, entity.getGenreId());
        stmt.bindString(6, entity.getName());
        stmt.bindLong(7, entity.getDuration());
        stmt.bindDouble(8, entity.getTempo());
        stmt.bindString(9, entity.getArtist());
        stmt.bindString(10, entity.getImage());
 
        String previewUrl = entity.getPreviewUrl();
        if (previewUrl != null) {
            stmt.bindString(11, previewUrl);
        }
        stmt.bindDouble(12, entity.getAcousticness());
        stmt.bindDouble(13, entity.getDanceability());
        stmt.bindDouble(14, entity.getEnergy());
        stmt.bindDouble(15, entity.getInstrumentalness());
        stmt.bindDouble(16, entity.getLiveness());
        stmt.bindDouble(17, entity.getLoudness());
        stmt.bindLong(18, entity.getPopularity());
        stmt.bindDouble(19, entity.getSpeechiness());
        stmt.bindDouble(20, entity.getValence());
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(21, date);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Song entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getId());
        stmt.bindString(2, entity.getIdPlaylist());
        stmt.bindString(3, entity.getIdSpotify());
        stmt.bindString(4, entity.getUrlSpotify());
        stmt.bindString(5, entity.getGenreId());
        stmt.bindString(6, entity.getName());
        stmt.bindLong(7, entity.getDuration());
        stmt.bindDouble(8, entity.getTempo());
        stmt.bindString(9, entity.getArtist());
        stmt.bindString(10, entity.getImage());
 
        String previewUrl = entity.getPreviewUrl();
        if (previewUrl != null) {
            stmt.bindString(11, previewUrl);
        }
        stmt.bindDouble(12, entity.getAcousticness());
        stmt.bindDouble(13, entity.getDanceability());
        stmt.bindDouble(14, entity.getEnergy());
        stmt.bindDouble(15, entity.getInstrumentalness());
        stmt.bindDouble(16, entity.getLiveness());
        stmt.bindDouble(17, entity.getLoudness());
        stmt.bindLong(18, entity.getPopularity());
        stmt.bindDouble(19, entity.getSpeechiness());
        stmt.bindDouble(20, entity.getValence());
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(21, date);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    @Override
    public Song readEntity(Cursor cursor, int offset) {
        Song entity = new Song( //
            cursor.getString(offset + 0), // id
            cursor.getString(offset + 1), // idPlaylist
            cursor.getString(offset + 2), // idSpotify
            cursor.getString(offset + 3), // urlSpotify
            cursor.getString(offset + 4), // genreId
            cursor.getString(offset + 5), // name
            cursor.getLong(offset + 6), // duration
            cursor.getFloat(offset + 7), // tempo
            cursor.getString(offset + 8), // artist
            cursor.getString(offset + 9), // image
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // previewUrl
            cursor.getFloat(offset + 11), // acousticness
            cursor.getFloat(offset + 12), // danceability
            cursor.getFloat(offset + 13), // energy
            cursor.getFloat(offset + 14), // instrumentalness
            cursor.getFloat(offset + 15), // liveness
            cursor.getFloat(offset + 16), // loudness
            cursor.getInt(offset + 17), // popularity
            cursor.getFloat(offset + 18), // speechiness
            cursor.getFloat(offset + 19), // valence
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20) // date
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Song entity, int offset) {
        entity.setId(cursor.getString(offset + 0));
        entity.setIdPlaylist(cursor.getString(offset + 1));
        entity.setIdSpotify(cursor.getString(offset + 2));
        entity.setUrlSpotify(cursor.getString(offset + 3));
        entity.setGenreId(cursor.getString(offset + 4));
        entity.setName(cursor.getString(offset + 5));
        entity.setDuration(cursor.getLong(offset + 6));
        entity.setTempo(cursor.getFloat(offset + 7));
        entity.setArtist(cursor.getString(offset + 8));
        entity.setImage(cursor.getString(offset + 9));
        entity.setPreviewUrl(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setAcousticness(cursor.getFloat(offset + 11));
        entity.setDanceability(cursor.getFloat(offset + 12));
        entity.setEnergy(cursor.getFloat(offset + 13));
        entity.setInstrumentalness(cursor.getFloat(offset + 14));
        entity.setLiveness(cursor.getFloat(offset + 15));
        entity.setLoudness(cursor.getFloat(offset + 16));
        entity.setPopularity(cursor.getInt(offset + 17));
        entity.setSpeechiness(cursor.getFloat(offset + 18));
        entity.setValence(cursor.getFloat(offset + 19));
        entity.setDate(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
     }
    
    @Override
    protected final String updateKeyAfterInsert(Song entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(Song entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Song entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "songs" to-many relationship of AttPlaylist. */
    public List<Song> _queryAttPlaylist_Songs(String idPlaylist) {
        synchronized (this) {
            if (attPlaylist_SongsQuery == null) {
                QueryBuilder<Song> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.IdPlaylist.eq(null));
                queryBuilder.orderRaw("T.'NAME' ASC");
                attPlaylist_SongsQuery = queryBuilder.build();
            }
        }
        Query<Song> query = attPlaylist_SongsQuery.forCurrentThread();
        query.setParameter(0, idPlaylist);
        return query.list();
    }

}
