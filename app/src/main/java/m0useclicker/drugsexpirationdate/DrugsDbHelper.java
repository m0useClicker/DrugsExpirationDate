package m0useclicker.drugsexpirationdate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class DrugsDbHelper extends SQLiteOpenHelper {
    public static final Integer DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Drugs.db";

    private static final String TEXT_COLUMN_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_CATEGORIES_TABLE =
            "CREATE TABLE " + DrugsDbContract.DrugCategoryEntry.TABLE_NAME + " (" +
                    DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID + TEXT_COLUMN_TYPE + " PRIMARY KEY)";
    private static final String SQL_CREATE_DRUGS_TABLE =
            "CREATE TABLE " + DrugsDbContract.DrugEntry.TABLE_NAME + " (" +
                    DrugsDbContract.DrugEntry.COLUMN_NAME_CATEGORY + TEXT_COLUMN_TYPE + COMMA_SEP +
                    DrugsDbContract.DrugEntry.COLUMN_NAME_DRUG_NAME + TEXT_COLUMN_TYPE + COMMA_SEP +
                    DrugsDbContract.DrugEntry.COLUMN_NAME_EXPIRATION_DATE + TEXT_COLUMN_TYPE + COMMA_SEP +
                    "FOREIGN KEY(" + DrugsDbContract.DrugEntry.COLUMN_NAME_CATEGORY + ") REFERENCES " + DrugsDbContract.DrugCategoryEntry.TABLE_NAME + "(" + DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID + ")" +
                    " )";

    public DrugsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("DB", "DB helper enter");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_DRUGS_TABLE);
        ContentValues values = new ContentValues();
        String[] categories = {"Pain", "Allergy", "Fewer", "Cold"};
        for (String category : categories) {
            values.put(DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID, category);
            db.insert(DrugsDbContract.DrugCategoryEntry.TABLE_NAME, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID};
        Cursor cursor = db.query(DrugsDbContract.DrugCategoryEntry.TABLE_NAME, columns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String itemId = cursor.getString(cursor.getColumnIndex(DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID));
                categories.add(itemId);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return categories;
    }

    public Multimap<String, Date> getDrugs(String category) throws ParseException {
        Multimap<String, Date> map = ArrayListMultimap.create();

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {DrugsDbContract.DrugEntry.COLUMN_NAME_DRUG_NAME, DrugsDbContract.DrugEntry.COLUMN_NAME_EXPIRATION_DATE};
        Cursor cursor = db.query(
                DrugsDbContract.DrugEntry.TABLE_NAME,
                columns,
                DrugsDbContract.DrugEntry.COLUMN_NAME_CATEGORY + "='" + category + "'",
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String itemId = cursor.getString(cursor.getColumnIndex(DrugsDbContract.DrugEntry.COLUMN_NAME_DRUG_NAME));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date expirationDate = formatter.parse(cursor.getString(cursor.getColumnIndex(DrugsDbContract.DrugEntry.COLUMN_NAME_EXPIRATION_DATE)));

                map.put(itemId, expirationDate);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return map;
    }
}
