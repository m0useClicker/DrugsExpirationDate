package m0useclicker.drugsexpirationdate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

class DrugsDbHelper extends SQLiteOpenHelper {
    public static final String[] categories = {"Pain", "Allergy", "Fewer", "Cold"};
    public static final Integer DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Drugs.db";

    private static final String TEXT_COLUMN_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";
    private static final String SQL_CREATE_CATEGORIES_TABLE =
            "CREATE TABLE " + DrugsDbContract.DrugCategoryEntry.TABLE_NAME + " (" +
                    DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID + TEXT_COLUMN_TYPE + " PRIMARY KEY)";
    private static final String SQL_CREATE_DRUGS_TABLE =
            "CREATE TABLE " + DrugsDbContract.DrugEntry.TABLE_NAME + " (" +
                    DrugsDbContract.DrugEntry.COLUMN_NAME_CATEGORY + TEXT_COLUMN_TYPE + COMMA_SEP +
                    DrugsDbContract.DrugEntry.COLUMN_NAME_DRUG_NAME + TEXT_COLUMN_TYPE + COMMA_SEP +
                    DrugsDbContract.DrugEntry.COLUMN_NAME_EXPIRATION_DATE + " INTEGER," +
                    "FOREIGN KEY(" + DrugsDbContract.DrugEntry.COLUMN_NAME_CATEGORY + ") REFERENCES " + DrugsDbContract.DrugCategoryEntry.TABLE_NAME + "(" + DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID + ")" +
                    " ON DELETE CASCADE ON UPDATE CASCADE)";
    private static final String SQL_CATEGORY_WHERE_CLAUSE = DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID + "=?";
    private static final String SQL_DRUG_WHERE_CLAUSE = DrugsDbContract.DrugEntry.COLUMN_NAME_CATEGORY + "=? AND " + DrugsDbContract.DrugEntry.COLUMN_NAME_DRUG_NAME + "=?";

    public DrugsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_DRUGS_TABLE);
        ContentValues values = new ContentValues();

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

    public ArrayList<Drug> getDrugs(String category) {
        ArrayList<Drug> drugs = new ArrayList<>();

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
                Date date = new Date(cursor.getLong(cursor.getColumnIndex(DrugsDbContract.DrugEntry.COLUMN_NAME_EXPIRATION_DATE)));
                drugs.add(new Drug(itemId, date));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return drugs;
    }

    public boolean addCategory(String categoryName) {
        if(isCategoryExist(categoryName)){
            return false;
        }
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID, categoryName);

        db.insert(DrugsDbContract.DrugCategoryEntry.TABLE_NAME, null, values);
        return true;
    }

    public void removeCategory(String category) {
        SQLiteDatabase db = getWritableDatabase();

        String[] whereArguments = {category};

        db.delete(DrugsDbContract.DrugCategoryEntry.TABLE_NAME, SQL_CATEGORY_WHERE_CLAUSE, whereArguments);
    }

    public void removeDrug(String categoryName, String drugName) {
        SQLiteDatabase db = getReadableDatabase();

        String[] whereArguments = {categoryName, drugName};

        db.delete(DrugsDbContract.DrugEntry.TABLE_NAME, SQL_DRUG_WHERE_CLAUSE, whereArguments);
    }

    public void renameCategory(String currentName, String newName) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID, newName);

        String[] whereArguments = {currentName};

        db.update(DrugsDbContract.DrugCategoryEntry.TABLE_NAME, values, SQL_CATEGORY_WHERE_CLAUSE, whereArguments);
    }

    public void renameDrug(String categoryName, String currentName, String newName) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DrugsDbContract.DrugEntry.COLUMN_NAME_DRUG_NAME, newName);

        String[] whereArguments = {categoryName, currentName};

        db.update(DrugsDbContract.DrugEntry.TABLE_NAME, values, SQL_DRUG_WHERE_CLAUSE, whereArguments);
    }

    public void changeDrugDate(String categoryName, String drugName, long totalMilliseconds) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DrugsDbContract.DrugEntry.COLUMN_NAME_EXPIRATION_DATE, totalMilliseconds);

        String[] whereArguments = {categoryName, drugName};

        db.update(DrugsDbContract.DrugEntry.TABLE_NAME, values, SQL_DRUG_WHERE_CLAUSE, whereArguments);
    }

    public void addDrug(String categoryName, String drugName, long dateInMilliseconds) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DrugsDbContract.DrugEntry.COLUMN_NAME_CATEGORY, categoryName);
        values.put(DrugsDbContract.DrugEntry.COLUMN_NAME_DRUG_NAME, drugName);
        values.put(DrugsDbContract.DrugEntry.COLUMN_NAME_EXPIRATION_DATE, dateInMilliseconds);

        db.insert(DrugsDbContract.DrugEntry.TABLE_NAME, null, values);
    }

    private boolean isCategoryExist(String categoryName) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID};
        String[] whereArguments = {categoryName};

        Cursor cursor = db.query(DrugsDbContract.DrugCategoryEntry.TABLE_NAME, columns, SQL_CATEGORY_WHERE_CLAUSE, whereArguments, null, null, null);

        Boolean rowExists = cursor.moveToFirst();

        cursor.close();
        return rowExists;
    }
}
