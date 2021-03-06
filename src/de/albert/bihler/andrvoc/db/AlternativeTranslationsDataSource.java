package de.albert.bihler.andrvoc.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AlternativeTranslationsDataSource {

    private static final String TAG = "AlternativeTranslationsDataSource";

    private SQLiteDatabase database;
    private final DbOpenHelper dbHelper;
    private final Context ctx;

    public AlternativeTranslationsDataSource(Context context) {
        this.ctx = context;
        dbHelper = new DbOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<String> getAlternativeTranslations(long vocabularyId) {
        Log.i(TAG, "Loading alternative translations for word " + vocabularyId);
        List<String> altTranslations = new ArrayList<String>();

        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME_ALTTRANSLATIONS, DbOpenHelper.ALL_COLUMNS_ALTTRANSLATIONS,
                DbOpenHelper.AltTranslationsColumn.VOCABULARY_ID + "=" + vocabularyId, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            altTranslations.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();

        return altTranslations;
    }

    public void saveAlternativeTranslations(List<String> altTranslations, long vocabularyId) {
        Log.i(TAG, "Saving " + altTranslations.size() + " alternative translations for word " + vocabularyId);
        for (String altTranslation : altTranslations) {
            ContentValues values = new ContentValues();
            values.put(DbOpenHelper.AltTranslationsColumn.TRANSLATION, altTranslation);
            values.put(DbOpenHelper.AltTranslationsColumn.VOCABULARY_ID, vocabularyId);
            database.insert(DbOpenHelper.TABLE_NAME_ALTTRANSLATIONS, null, values);
        }
    }

}
