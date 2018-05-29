package es.app.attune.attune.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ATT_PLAYLIST".
*/
public class AttPlaylistDao extends AbstractDao<AttPlaylist, String> {

    public static final String TABLENAME = "ATT_PLAYLIST";

    /**
     * Properties of entity AttPlaylist.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Tempo = new Property(2, float.class, "tempo", false, "TEMPO");
        public final static Property Duration = new Property(3, int.class, "duration", false, "DURATION");
        public final static Property Image = new Property(4, byte[].class, "image", false, "IMAGE");
        public final static Property Genre = new Property(5, String.class, "genre", false, "GENRE");
        public final static Property Creation_date = new Property(6, java.util.Date.class, "creation_date", false, "CREATION_DATE");
        public final static Property Acousticness = new Property(7, float.class, "acousticness", false, "ACOUSTICNESS");
        public final static Property Danceability = new Property(8, float.class, "danceability", false, "DANCEABILITY");
        public final static Property Energy = new Property(9, float.class, "energy", false, "ENERGY");
        public final static Property Instrumentalness = new Property(10, float.class, "instrumentalness", false, "INSTRUMENTALNESS");
        public final static Property Liveness = new Property(11, float.class, "liveness", false, "LIVENESS");
        public final static Property Loudness = new Property(12, float.class, "loudness", false, "LOUDNESS");
        public final static Property Popularity = new Property(13, int.class, "popularity", false, "POPULARITY");
        public final static Property Speechiness = new Property(14, float.class, "speechiness", false, "SPEECHINESS");
        public final static Property Valence = new Property(15, float.class, "valence", false, "VALENCE");
    }

    private DaoSession daoSession;


    public AttPlaylistDao(DaoConfig config) {
        super(config);
    }
    
    public AttPlaylistDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ATT_PLAYLIST\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"NAME\" TEXT NOT NULL ," + // 1: name
                "\"TEMPO\" REAL NOT NULL ," + // 2: tempo
                "\"DURATION\" INTEGER NOT NULL ," + // 3: duration
                "\"IMAGE\" BLOB NOT NULL ," + // 4: image
                "\"GENRE\" TEXT NOT NULL ," + // 5: genre
                "\"CREATION_DATE\" INTEGER NOT NULL ," + // 6: creation_date
                "\"ACOUSTICNESS\" REAL NOT NULL ," + // 7: acousticness
                "\"DANCEABILITY\" REAL NOT NULL ," + // 8: danceability
                "\"ENERGY\" REAL NOT NULL ," + // 9: energy
                "\"INSTRUMENTALNESS\" REAL NOT NULL ," + // 10: instrumentalness
                "\"LIVENESS\" REAL NOT NULL ," + // 11: liveness
                "\"LOUDNESS\" REAL NOT NULL ," + // 12: loudness
                "\"POPULARITY\" INTEGER NOT NULL ," + // 13: popularity
                "\"SPEECHINESS\" REAL NOT NULL ," + // 14: speechiness
                "\"VALENCE\" REAL NOT NULL );"); // 15: valence
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_ATT_PLAYLIST_ID ON \"ATT_PLAYLIST\"" +
                " (\"ID\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ATT_PLAYLIST\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AttPlaylist entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getId());
        stmt.bindString(2, entity.getName());
        stmt.bindDouble(3, entity.getTempo());
        stmt.bindLong(4, entity.getDuration());
        stmt.bindBlob(5, entity.getImage());
        stmt.bindString(6, entity.getGenre());
        stmt.bindLong(7, entity.getCreation_date().getTime());
        stmt.bindDouble(8, entity.getAcousticness());
        stmt.bindDouble(9, entity.getDanceability());
        stmt.bindDouble(10, entity.getEnergy());
        stmt.bindDouble(11, entity.getInstrumentalness());
        stmt.bindDouble(12, entity.getLiveness());
        stmt.bindDouble(13, entity.getLoudness());
        stmt.bindLong(14, entity.getPopularity());
        stmt.bindDouble(15, entity.getSpeechiness());
        stmt.bindDouble(16, entity.getValence());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AttPlaylist entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getId());
        stmt.bindString(2, entity.getName());
        stmt.bindDouble(3, entity.getTempo());
        stmt.bindLong(4, entity.getDuration());
        stmt.bindBlob(5, entity.getImage());
        stmt.bindString(6, entity.getGenre());
        stmt.bindLong(7, entity.getCreation_date().getTime());
        stmt.bindDouble(8, entity.getAcousticness());
        stmt.bindDouble(9, entity.getDanceability());
        stmt.bindDouble(10, entity.getEnergy());
        stmt.bindDouble(11, entity.getInstrumentalness());
        stmt.bindDouble(12, entity.getLiveness());
        stmt.bindDouble(13, entity.getLoudness());
        stmt.bindLong(14, entity.getPopularity());
        stmt.bindDouble(15, entity.getSpeechiness());
        stmt.bindDouble(16, entity.getValence());
    }

    @Override
    protected final void attachEntity(AttPlaylist entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    @Override
    public AttPlaylist readEntity(Cursor cursor, int offset) {
        AttPlaylist entity = new AttPlaylist( //
            cursor.getString(offset + 0), // id
            cursor.getString(offset + 1), // name
            cursor.getFloat(offset + 2), // tempo
            cursor.getInt(offset + 3), // duration
            cursor.getBlob(offset + 4), // image
            cursor.getString(offset + 5), // genre
            new java.util.Date(cursor.getLong(offset + 6)), // creation_date
            cursor.getFloat(offset + 7), // acousticness
            cursor.getFloat(offset + 8), // danceability
            cursor.getFloat(offset + 9), // energy
            cursor.getFloat(offset + 10), // instrumentalness
            cursor.getFloat(offset + 11), // liveness
            cursor.getFloat(offset + 12), // loudness
            cursor.getInt(offset + 13), // popularity
            cursor.getFloat(offset + 14), // speechiness
            cursor.getFloat(offset + 15) // valence
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AttPlaylist entity, int offset) {
        entity.setId(cursor.getString(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setTempo(cursor.getFloat(offset + 2));
        entity.setDuration(cursor.getInt(offset + 3));
        entity.setImage(cursor.getBlob(offset + 4));
        entity.setGenre(cursor.getString(offset + 5));
        entity.setCreation_date(new java.util.Date(cursor.getLong(offset + 6)));
        entity.setAcousticness(cursor.getFloat(offset + 7));
        entity.setDanceability(cursor.getFloat(offset + 8));
        entity.setEnergy(cursor.getFloat(offset + 9));
        entity.setInstrumentalness(cursor.getFloat(offset + 10));
        entity.setLiveness(cursor.getFloat(offset + 11));
        entity.setLoudness(cursor.getFloat(offset + 12));
        entity.setPopularity(cursor.getInt(offset + 13));
        entity.setSpeechiness(cursor.getFloat(offset + 14));
        entity.setValence(cursor.getFloat(offset + 15));
     }
    
    @Override
    protected final String updateKeyAfterInsert(AttPlaylist entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(AttPlaylist entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AttPlaylist entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
