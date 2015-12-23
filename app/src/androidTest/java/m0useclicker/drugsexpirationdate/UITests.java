package m0useclicker.drugsexpirationdate;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class UITests extends ActivityInstrumentationTestCase2<mainActivity> {
    private final static String testDataId = "_uiTesting";
    private mainActivity activity;

    public UITests() {
        super(mainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        activity = getActivity();
        removeTestData();
    }

    @After
    public void cleanUp() {
        removeTestData();
    }

    private void removeTestData() {
        DrugsDbHelper dbHelper = new DrugsDbHelper(activity);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(DrugsDbContract.DrugCategoryEntry.TABLE_NAME, DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID + " LIKE '%" + testDataId + "'", null);
        database.close();
        dbHelper.close();
    }

    @Test
    public void testAddCategoryCanBeShowedSeveralTimes() {
        final String addCategory1Name = "createCategory1" + getUniqueSuffix();
        final String addCategory2Name = "createCategory2" + getUniqueSuffix();
        onView(withId(R.id.addCategoryButton)).perform(click());
        onView(withHint(R.string.newCategoryHint)).perform(typeText(addCategory1Name));
        onView(withText(R.string.add)).perform(click());

        onView(withId(R.id.addCategoryButton)).perform(click());
        onView(withHint(R.string.newCategoryHint)).perform(typeText(addCategory2Name));
        onView(withText(R.string.add)).perform(click());

        onView(withText(addCategory1Name)).check(matches(isDisplayed()));
        onView(withText(addCategory2Name)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddCategoryOk() {
        final String addCategoryName = "createCategoryOk" + getUniqueSuffix();
        onView(withId(R.id.addCategoryButton)).perform(click());
        onView(withHint(R.string.newCategoryHint)).perform(typeText(addCategoryName));
        onView(withText(R.string.add)).perform(click());

        onView(withText(addCategoryName)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddCategoryCancel() {
        final String addCategoryName = "createCategoryCancel" + getUniqueSuffix();
        onView(withId(R.id.addCategoryButton)).perform(click());
        onView(withHint(R.string.newCategoryHint)).perform(typeText(addCategoryName));
        onView(withText(R.string.cancel)).perform(click());

        onView(withText(addCategoryName)).check(doesNotExist());
    }

    @Test
    public void testRenameCategoryDialogCanBeShowedSeveralTimes() {
        final String rename1This = "renameMe1" + getUniqueSuffix();
        final String rename2This = "renameMe2" + getUniqueSuffix();
        addCategory(rename1This);
        addCategory(rename2This);

        String newName1 = "newName" + rename1This;
        String newName2 = "newName" + rename2This;

        onView(withText(rename1This)).perform(longClick());
        onView(withText(R.string.rename)).perform(click());
        onView(withText(rename1This)).perform(replaceText(newName1));
        onView(withText(R.string.rename)).perform(click());

        onView(withText(rename2This)).perform(longClick());
        onView(withText(R.string.rename)).perform(click());
        onView(withText(rename2This)).perform(replaceText(newName2));
        onView(withText(R.string.rename)).perform(click());

        onView(withText(newName1)).check(matches(isDisplayed()));
        onView(withText(newName2)).check(matches(isDisplayed()));
    }

    @Test
    public void testRenameCategoryOk() {
        final String renameThis = "renameMeOk" + getUniqueSuffix();
        addCategory(renameThis);

        String newName = "newName" + renameThis;
        onView(withText(renameThis)).perform(longClick());
        onView(withText(R.string.rename)).perform(click());
        onView(withText(renameThis)).perform(replaceText(newName));
        onView(withText(R.string.rename)).perform(click());

        onView(withText(newName)).check(matches(isDisplayed()));
        onView(withText(renameThis)).check(doesNotExist());
    }

    @Test
    public void testRenameCategoryCancel() {
        final String renameThis = "renameThisCancel" + getUniqueSuffix();
        addCategory(renameThis);

        String newName = renameThis + "newName";
        onView(withText(renameThis)).perform(longClick());
        onView(withText(R.string.rename)).perform(click());
        onView(withText(renameThis)).perform(replaceText(newName));
        onView(withText(R.string.cancel)).perform(click());

        onView(withText(renameThis)).check(matches(isDisplayed()));
        onView(withText(newName)).check(doesNotExist());
    }

    @Test
    public void testDeleteCategory() {
        final String deleteThis = "deleteCategory" + getUniqueSuffix();
        addCategory(deleteThis);

        onView(withText(deleteThis)).perform(longClick());
        onView(withText(R.string.delete)).perform(click());

        onView(withText(deleteThis)).check(doesNotExist());
    }

    @Test
    public void testAddDrugDialogCanBeShowedSeveralTimes(){
        final String category = "addDrugOkCategory"+getUniqueSuffix();
        final String drug1Name = "add1Drug"+getUniqueSuffix();
        final String drug2Name = "add2Drug"+getUniqueSuffix();
        addCategory(category);

        onView(withText(category)).perform(longClick());
        onView(withText(R.string.addDrugMenuItem)).perform(click());
        onView(withHint(R.string.addDrugNameHint)).perform(typeText(drug1Name));
        onView(withText(R.string.setExpirationDateDialogButton)).perform(click());
        onView(withText(R.string.ok)).perform(click());
        onView(withText(category)).perform(longClick());
        onView(withText(R.string.addDrugMenuItem)).perform(click());
        onView(withHint(R.string.addDrugNameHint)).perform(typeText(drug2Name));
        onView(withText(R.string.setExpirationDateDialogButton)).perform(click());
        onView(withText(R.string.ok)).perform(click());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        String date = dateFormat.format(new Date());

        onView(allOf(withText(drug1Name), hasSibling(hasDescendant(withText(date))))).check(matches(isDisplayed()));
        onView(allOf(withText(drug2Name), hasSibling(hasDescendant(withText(date))))).check(matches(isDisplayed()));
    }

    @Test
    public void testAddDrugOk() {
        final String category = "addDrugOkCategory" + getUniqueSuffix();
        final String drugName = "addDrugOk" + getUniqueSuffix();
        addCategory(category);

        onView(withText(category)).perform(longClick());
        onView(withText(R.string.addDrugMenuItem)).perform(click());
        onView(withHint(R.string.addDrugNameHint)).perform(typeText(drugName));
        onView(withText(R.string.setExpirationDateDialogButton)).perform(click());
        onView(withText(R.string.ok)).perform(click());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        String date = dateFormat.format(new Date());

        onView(allOf(withText(drugName), hasSibling(hasDescendant(withText(date))))).check(matches(isDisplayed()));
    }

    @Test
    public void testAddDrugCancelOnDrugName(){
        final String category = "addDrugCancelNameCategory"+getUniqueSuffix();
        final String drugName = "addDrugCancelName"+getUniqueSuffix();
        addCategory(category);

        onView(withText(category)).perform(longClick());
        onView(withText(R.string.addDrugMenuItem)).perform(click());
        onView(withHint(R.string.addDrugNameHint)).perform(typeText(drugName));
        onView(withText(R.string.cancel)).perform(click());

        onView(withText(drugName)).check(doesNotExist());
    }

    @Test
    public void testAddDrugCancelOnDrugDate(){
        final String category = "addDrugCancelDateCategory"+getUniqueSuffix();
        final String drugName = "addDrugCancelDate"+getUniqueSuffix();
        addCategory(category);

        onView(withText(category)).perform(longClick());
        onView(withText(R.string.addDrugMenuItem)).perform(click());
        onView(withHint(R.string.addDrugNameHint)).perform(typeText(drugName));
        onView(withText(R.string.setExpirationDateDialogButton)).perform(click());
        onView(withText(R.string.cancel)).perform(click());

        onView(withText(drugName)).check(doesNotExist());
    }

    @Test
    public void testChangeDateDialogCanBeShowedSeveralTimes(){
        String categoryName = "setDateTwoDrugsCategory" + getUniqueSuffix();
        String drug1Name = "rename1Drug" + getUniqueSuffix();
        String drug2Name = "rename2Drug" + getUniqueSuffix();
        addDrug(categoryName, drug1Name, 0);
        addDrug(categoryName, drug2Name, 0);

        onView(withText(categoryName)).perform(click());
        onView(withText(drug1Name)).perform(longClick());
        onView(withText(R.string.changeDateMenuItem)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withText(drug2Name)).perform(longClick());
        onView(withText(R.string.changeDateMenuItem)).perform(click());
        onView(withText("OK")).perform(click());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        String date = dateFormat.format(new Date());

        onView(allOf(withText(drug1Name),hasSibling(hasDescendant(withText(date))))).check(matches(isDisplayed()));
        onView(allOf(withText(drug2Name),hasSibling(hasDescendant(withText(date))))).check(matches(isDisplayed()));
    }

    @Test
    public void testChangeDateOk() {
        String categoryName = "setDateDrugOkCategory" + getUniqueSuffix();
        String drugName = "renameDrugOk" + getUniqueSuffix();
        addDrug(categoryName, drugName, 0);

        onView(withText(categoryName)).perform(click());
        onView(withText(drugName)).perform(longClick());
        onView(withText(R.string.changeDateMenuItem)).perform(click());
        onView(withText("OK")).perform(click());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        String date = dateFormat.format(new Date());

        onView(withText("Dec 31, 1969")).check(doesNotExist());
        onView(withText(date)).check(matches(isDisplayed()));
    }

    @Test
    public void testChangeDateCancel() {
        String categoryName = "setDateDrugCancelCategory" + getUniqueSuffix();
        String drugName = "renameDrugOk" + getUniqueSuffix();
        addDrug(categoryName, drugName, 0);

        onView(withText(categoryName)).perform(click());
        onView(withText(drugName)).perform(longClick());
        onView(withText(R.string.changeDateMenuItem)).perform(click());
        onView(withText(R.string.cancel)).perform(click());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        String date = dateFormat.format(new Date());

        onView(allOf(withText(activity.getString(R.string.expiresLabelText)), hasSibling(withText("Dec 31, 1969")))).check(matches(isDisplayed()));
        onView(allOf(withText(activity.getString(R.string.expiresLabelText)), hasSibling(withText(date)))).check(doesNotExist());
    }

    @Test
    public void testRenameDrugDialogCanBeShowedSeveralTimes(){
        String categoryName = "renameTwoDrugsCategory" + getUniqueSuffix();
        String drug1Name = "rename1Drug" + getUniqueSuffix();
        String drug2Name = "rename2Drug" + getUniqueSuffix();
        String new1Name = "newName" + drug1Name;
        String new2Name = "newName" + drug2Name;
        addDrug(categoryName, drug1Name, 0);
        addDrug(categoryName, drug2Name, 0);

        onView(withText(categoryName)).perform(click());
        onView(withText(drug1Name)).perform(longClick());
        onView(withText(R.string.rename)).perform(click());
        onView(withText(drug1Name)).perform(replaceText(new1Name));
        onView(withText(R.string.rename)).perform(click());

        onView(withText(drug2Name)).perform(longClick());
        onView(withText(R.string.rename)).perform(click());
        onView(withText(drug2Name)).perform(replaceText(new2Name));
        onView(withText(R.string.rename)).perform(click());

        onView(withText(new1Name)).check(matches(isDisplayed()));
        onView(withText(new2Name)).check(matches(isDisplayed()));
    }

    @Test
    public void testRenameDrugOk() {
        String categoryName = "renameDrugOkCategory" + getUniqueSuffix();
        String drugName = "renameDrugOk" + getUniqueSuffix();
        String newName = "newName" + drugName;
        addDrug(categoryName, drugName, 0);

        onView(withText(categoryName)).perform(click());
        onView(withText(drugName)).perform(longClick());
        onView(withText(R.string.rename)).perform(click());
        onView(withText(drugName)).perform(replaceText(newName));
        onView(withText(R.string.rename)).perform(click());

        onView(withText(drugName)).check(doesNotExist());
        onView(withText(newName)).check(matches(isDisplayed()));
    }

    @Test
    public void testRenameDrugCancel() {
        String categoryName = "renameDrugCancelCategory" + getUniqueSuffix();
        String drugName = "renameDrugCancel" + getUniqueSuffix();
        String newName = "newName" + drugName;
        addDrug(categoryName, drugName, 0);

        onView(withText(categoryName)).perform(click());
        onView(withText(drugName)).perform(longClick());
        onView(withText(R.string.rename)).perform(click());
        onView(withText(drugName)).perform(replaceText(newName));
        onView(withText(R.string.cancel)).perform(click());

        onView(withText(drugName)).check(matches(isDisplayed()));
        onView(withText(newName)).check(doesNotExist());
    }

    @Test
    public void testDeleteDrug() {
        String categoryName = "deleteDrugCategory" + getUniqueSuffix();
        String drugName = "deleteDrug" + getUniqueSuffix();
        addDrug(categoryName, drugName, 0);

        onView(withText(categoryName)).perform(click());
        onView(withText(drugName)).perform(longClick());
        onView(withText(R.string.delete)).perform(click());

        onView(withText(drugName)).check(doesNotExist());
    }

    private void addCategory(String categoryName) {
        insertCategoryRow(categoryName);
        updateList();
    }

    private void addDrug(String categoryName, String drugName, Integer expirationDate) {
        insertCategoryRow(categoryName);
        DrugsDbHelper dbHelper = new DrugsDbHelper(activity);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DrugsDbContract.DrugEntry.COLUMN_NAME_CATEGORY, categoryName);
        values.put(DrugsDbContract.DrugEntry.COLUMN_NAME_DRUG_NAME, drugName);
        values.put(DrugsDbContract.DrugEntry.COLUMN_NAME_EXPIRATION_DATE, expirationDate);
        database.insert(DrugsDbContract.DrugEntry.TABLE_NAME, null, values);
        database.close();
        dbHelper.close();

        updateList();
    }

    private void insertCategoryRow(String categoryName) {
        DrugsDbHelper dbHelper = new DrugsDbHelper(activity);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DrugsDbContract.DrugCategoryEntry.COLUMN_NAME_ID, categoryName);
        database.insert(DrugsDbContract.DrugCategoryEntry.TABLE_NAME, null, values);
        database.close();
        dbHelper.close();
    }

    private void updateList() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView listView = (ListView) activity.findViewById(R.id.listView);
                activity.listAdapter.refreshData();
                activity.listAdapter.notifyDataSetChanged();
                listView.invalidateViews();
            }
        });
    }

    private static String getUniqueSuffix() {
        return String.valueOf(Calendar.getInstance().getTime().getTime()) + testDataId;
    }
}
