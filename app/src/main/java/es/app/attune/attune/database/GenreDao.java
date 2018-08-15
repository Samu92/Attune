package es.app.attune.attune.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GENRE".
*/
public class GenreDao extends AbstractDao<Genre, String> {

    public static final String TABLENAME = "GENRE";

    /**
     * Properties of entity Genre.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Manual = new Property(2, boolean.class, "manual", false, "MANUAL");
    }


    public GenreDao(DaoConfig config) {
        super(config);
    }
    
    public GenreDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GENRE\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"NAME\" TEXT NOT NULL ," + // 1: name
                "\"MANUAL\" INTEGER NOT NULL );"); // 2: manual
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_GENRE_ID ON \"GENRE\"" +
                " (\"ID\" ASC);");
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_GENRE_NAME ON \"GENRE\"" +
                " (\"NAME\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GENRE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Genre entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getId());
        stmt.bindString(2, entity.getName());
        stmt.bindLong(3, entity.getManual() ? 1L : 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Genre entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getId());
        stmt.bindString(2, entity.getName());
        stmt.bindLong(3, entity.getManual() ? 1L : 0L);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    @Override
    public Genre readEntity(Cursor cursor, int offset) {
        Genre entity = new Genre( //
            cursor.getString(offset + 0), // id
                cursor.getString(offset + 1), // name
                cursor.getShort(offset + 2) != 0 // manual
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Genre entity, int offset) {
        entity.setId(cursor.getString(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setManual(cursor.getShort(offset + 2) != 0);
     }
    
    @Override
    protected final String updateKeyAfterInsert(Genre entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(Genre entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Genre entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}