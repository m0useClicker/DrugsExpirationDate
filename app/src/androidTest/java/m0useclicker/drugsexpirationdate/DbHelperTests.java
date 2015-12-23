package m0useclicker.drugsexpirationdate;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.util.Calendar;

public class DbHelperTests extends AndroidTestCase {
    private DrugsDbHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        dbHelper = new DrugsDbHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public void tearDown() throws Exception {
        dbHelper.close();
        database.close();
        super.tearDown();
    }

    public void testAddCategory() {
        String categoryName = "db_newCategory";
        dbHelper.addCategory(categoryName);

        assertTrue(doesCategoryExist(categoryName));
    }

    public void testAddDrug() {
        String categoryName = "db_newDrug_category";
        String drugName = "db_newDrug";
        long drugExpiration = Calendar.getInstance().getTime().getTime();

        dbHelper.addDrug(categoryName, drugName, drugExpiration);

        assertTrue(doesDrugExist(categoryName, drugName, String.valueOf(drugExpiration)));
    }

    public void testRemoveCategory() {
        String categoryName = "db_newCategoryRemove";
        addCategory(categoryName);

        dbHelper.removeCategory(categoryName);

        assertFalse(doesCategoryExist(categoryName));
    }

    public void testRemoveDrug() {
        String categoryName = "db_removeDrug_category";
        String drugName = "db_removeDrug";
        long drugExpiration = Calendar.getInstance().getTime().getTime();

        addDrug(categoryName,drugName,String.valueOf(drugExpiration));

        dbHelper.removeDrug(categoryName, drugName);

        assertFalse(doesDrugExist(categoryName, drugName, String.valueOf(drugExpiration)));
    }

    public void testRenameCategory() {
        String categoryName = "db_newCategoryRename";
        String categoryNewName = categoryName + "newName";
        addCategory(categoryName);

        dbHelper.renameCategory(categoryName, categoryNewName);
        assertFalse(doesCategoryExist(categoryName));
        assertTrue(doesCategoryExist(categoryNewName));
    }

    public void testRenameDrug() {
        String categoryName = "db_renameDrug_category";
        String drugName = "db_renameDrug";
        String drugNewName = drugName + "newName";
        long drugExpiration = Calendar.getInstance().getTime().getTime();
        addDrug(categoryName, drugName, String.valueOf(drugExpiration));

        dbHelper.renameDrug(categoryName, drugName, drugNewName);

        assertFalse(doesDrugExist(categoryName, drugName, String.valueOf(drugExpiration)));
        assertTrue(doesDrugExist(categoryName, drugNewName, String.valueOf(drugExpiration)));
    }

    public void testChangeExpirationDate() {
        String categoryName = "db_newDateDrug_category";
        String drugName = "db_newDateDrug";
        long drugExpiration = 0;
        long drugNewDate = Calendar.getInstance().getTime().getTime();

        addDrug(categoryName, drugName, String.valueOf(drugExpiration));

        dbHelper.changeDrugDate(categoryName, drugName, drugNewDate);

        assertFalse(doesDrugExist(categoryName, drugName, String.valueOf(drugExpiration)));
        assertTrue(doesDrugExist(categoryName, drugName, String.valueOf(drugNewDate)));
    }

    private void addCategory(String categoryName) {
        ContentValues values = new ContentValues();
        values.put(DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID, categoryName);
        database.insert(DrugsDbContract.DrugCategoryEntry.TABLE_NAME, null, values);
    }

    private void addDrug(String categoryName, String drugName, String drugExpiration) {
        ContentValues values = new ContentValues();
        values.put(DrugsDbContract.DrugEntry.COLUMN_NAME_CATEGORY, categoryName);
        values.put(DrugsDbContract.DrugEntry.COLUMN_NAME_DRUG_NAME, drugName);
        values.put(DrugsDbContract.DrugEntry.COLUMN_NAME_EXPIRATION_DATE, String.valueOf(drugExpiration));
        database.insert(DrugsDbContract.DrugEntry.TABLE_NAME, null, values);
    }

    private Boolean doesCategoryExist(String categoryName) {
        String[] columns = {DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID};
        String[] values = {categoryName};
        Cursor cursor = database.query(DrugsDbContract.DrugCategoryEntry.TABLE_NAME, columns, DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID + "=?", values, null, null, null);

        Boolean hasRecords = cursor.moveToFirst();
        cursor.close();

        return hasRecords;
    }

    private Boolean doesDrugExist(String categoryName, String drugName, String drugExpirationDate) {
        String[] columns = {DrugsDbContract.DrugEntry.COLUMN_NAME_DRUG_NAME};
        String[] values = {categoryName, drugName, drugExpirationDate};
        Cursor cursor = database.query(
                DrugsDbContract.DrugEntry.TABLE_NAME,
                columns,
                DrugsDbContract.DrugEntry.COLUMN_NAME_CATEGORY + "=? AND " +
                        DrugsDbContract.DrugEntry.COLUMN_NAME_DRUG_NAME + "=? AND " +
                        DrugsDbContract.DrugEntry.COLUMN_NAME_EXPIRATION_DATE + "=?",
                values, null, null, null);

        Boolean hasRecords = cursor.moveToFirst();
        cursor.close();

        return hasRecords;
    }
}
