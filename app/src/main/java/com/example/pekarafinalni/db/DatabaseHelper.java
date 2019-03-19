package com.example.pekarafinalni.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.pekarafinalni.db.model.Peciva;
import com.example.pekarafinalni.db.model.Pekara;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "db.pekara.pekara";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    private Dao<Pekara, Integer> getmPekara = null;
    private Dao<Peciva, Integer> getmPeciva = null;

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Pekara.class);
            TableUtils.createTable(connectionSource, Peciva.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Peciva.class, true);
            TableUtils.dropTable(connectionSource, Pekara.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Pekara, Integer> getPekara() throws SQLException {
        if (getmPekara == null) {
            getmPekara = getDao(Pekara.class);
        }
        return getmPekara;
    }

    public Dao<Peciva, Integer> getPeciva() throws SQLException {
        if (getmPeciva == null) {
            getmPeciva = getDao(Peciva.class);
        }
        return getmPeciva;
    }

    @Override
    public void close() {
        getmPeciva = null;
        getmPekara = null;
        super.close();
    }
}