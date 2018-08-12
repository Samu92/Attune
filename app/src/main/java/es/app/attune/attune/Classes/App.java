package es.app.attune.attune.Classes;

import android.app.Application;
import android.content.Context;

import org.greenrobot.greendao.database.Database;

import es.app.attune.attune.Database.DaoMaster;
import es.app.attune.attune.Database.DaoSession;

public class App extends Application {
    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = true;

    private DaoSession daoSession;
    private static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "attune-db-encrypted" : "attune-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        /*DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "attune-db", null);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();*/

        //DaoMaster.dropAllTables(db,true);
        //DaoMaster.createAllTables(db,true);

        /* RELEASE */
        /*
        DaoMaster.OpenHelper helper = new DaoMaster.OpenHelper(this, ENCRYPTED ? "attune-db-encrypted" : "attune-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        */
    }

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
