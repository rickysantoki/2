package com.example.cowlogs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BreedLogsDB";
    private static final String LOG = "breed";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(BreedLogs.CREATE_TABLE);
        db.execSQL(User.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BreedLogs.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);

        this.onCreate(db);
    }

    public User createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(User.COLUMN_USERNAME, user.getUsername());
            contentValues.put(User.COLUMN_PASSWORD, user.getPassword());

            long id = db.insert(User.TABLE_NAME, null, contentValues);
            user.setId((int) id);
            return user;
        } catch (SQLException e) {
            db.close();
        } finally {
            db.close();
        }
        return null;
    }

    public BreedLogs createBreedLog(BreedLogs breedLogs) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(BreedLogs.COLUMN_BREED_ID, breedLogs.getbreedId());
            initialValues.put(BreedLogs.COLUMN_WEIGHT, breedLogs.getweight());
            initialValues.put(BreedLogs.COLUMN_AGE, breedLogs.getage());
            initialValues.put(BreedLogs.COLUMN_CONDITION, breedLogs.getcondition());
            initialValues.put(BreedLogs.COLUMN_START, breedLogs.getStart());
            initialValues.put(BreedLogs.COLUMN_BREED_INDEX, breedLogs.getBreedType());
            long id = db.insert(BreedLogs.TABLE_NAME, null, initialValues);
            breedLogs.setId((int) id);
            return breedLogs;
        } catch (SQLException e) {
            db.close();
        }

        return null;
    }

    public ArrayList<BreedLogs> getBreedLogsByBreedType(int breedType) {
        ArrayList<BreedLogs> BreedLogsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor c = db.query(BreedLogs.TABLE_NAME,
                    null,
                    BreedLogs.COLUMN_BREED_INDEX + "=?",
                    new String[]{String.valueOf(breedType)}, null, null, null, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    BreedLogs logs = new BreedLogs();
                    logs.setId(c.getInt((c.getColumnIndex(BreedLogs.COLUMN_ID))));
                    logs.setbreedId((c.getString(c.getColumnIndex(BreedLogs.COLUMN_BREED_ID))));
                    logs.setweight(c.getString(c.getColumnIndex(BreedLogs.COLUMN_WEIGHT)));
                    logs.setage(c.getString(c.getColumnIndex(BreedLogs.COLUMN_AGE)));
                    logs.setcondition(c.getString(c.getColumnIndex(BreedLogs.COLUMN_CONDITION)));
                    logs.setStart(c.getString(c.getColumnIndex(BreedLogs.COLUMN_START)));
                    logs.setBreedType((c.getInt(c.getColumnIndex(BreedLogs.COLUMN_BREED_INDEX))));
                    BreedLogsList.add(logs);
                } while (c.moveToNext());
            }
            c.close();
            return BreedLogsList;
        } catch (Exception $e) {
            Log.e("Query", $e.getMessage());
        } finally {
            db.close();
        }

        return null;
    }

    public ArrayList<BreedLogs> getAllBreedLogs() {
        ArrayList<BreedLogs> BreedLogsList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + BreedLogs.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor c = db.rawQuery(selectQuery, null)) {


            if (c.moveToFirst()) {
                do {
                    BreedLogs logs = new BreedLogs();
                    logs.setId(c.getInt((c.getColumnIndex(BreedLogs.COLUMN_ID))));
                    logs.setbreedId((c.getString(c.getColumnIndex(BreedLogs.COLUMN_BREED_ID))));
                    logs.setweight(c.getString(c.getColumnIndex(BreedLogs.COLUMN_WEIGHT)));
                    logs.setage(c.getString(c.getColumnIndex(BreedLogs.COLUMN_AGE)));
                    logs.setcondition(c.getString(c.getColumnIndex(BreedLogs.COLUMN_CONDITION)));
                    logs.setStart(c.getString(c.getColumnIndex(BreedLogs.COLUMN_START)));
                    logs.setBreedType((c.getInt(c.getColumnIndex(BreedLogs.COLUMN_BREED_INDEX))));
                    BreedLogsList.add(logs);
                } while (c.moveToNext());
            }
        }

        return BreedLogsList;
    }

}

