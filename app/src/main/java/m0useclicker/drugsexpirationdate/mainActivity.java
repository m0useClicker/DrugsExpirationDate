package m0useclicker.drugsexpirationdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;

import java.util.Calendar;

public class mainActivity extends Activity {
    private final int MENU_ADD_DRUG = 0;
    private final int MENU_ADD_REMINDER = 1;
    private final int MENU_EDIT_DATE = 2;
    private final int MENU_RENAME_CATEGORY = 3;
    private final int MENU_RENAME_DRUG = 4;
    private final int MENU_DELETE_CATEGORY = 5;
    private final int MENU_DELETE_DRUG=6;

    DrugsListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        ExpandableListView list = (ExpandableListView) findViewById(R.id.listView);
        registerForContextMenu(list);
        listAdapter = new DrugsListAdapter(this);
        list.setAdapter(listAdapter);
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type =
                ExpandableListView.getPackedPositionType(info.packedPosition);

        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            menu.setHeaderTitle("Drug");
            menu.add(0, MENU_ADD_REMINDER, 0, "Add calendar reminder");
            menu.add(0, MENU_EDIT_DATE, 0, "Change date");
            menu.add(0, MENU_RENAME_DRUG, 0, "Rename");
            menu.add(0, MENU_DELETE_DRUG, 1, "Delete");
        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            menu.setHeaderTitle("Category");
            menu.add(0, MENU_ADD_DRUG, 0, "Add drug");
            menu.add(0, MENU_RENAME_CATEGORY, 0, "Rename");
            menu.add(0, MENU_DELETE_CATEGORY, 1, "Delete");
        }
    }

    public boolean onContextItemSelected(MenuItem menuItem) {
        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) menuItem.getMenuInfo();

        int group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int child = ExpandableListView.getPackedPositionChild(info.packedPosition);

        switch (menuItem.getItemId()) {
            case MENU_DELETE_CATEGORY:
                listAdapter.removeCategory(group);
                return true;
            case MENU_DELETE_DRUG:
                listAdapter.removeDrug(group,child);
                return true;
            case MENU_RENAME_CATEGORY:
                showRenameCategoryDialog(group);
                return true;
            case MENU_RENAME_DRUG:
                showRenameDrugDialog(group,child);
                return true;
            case MENU_EDIT_DATE:
                setNewDate(group,child);
                return true;
            case MENU_ADD_DRUG:
                addDrug(group);
                return true;
            case MENU_ADD_REMINDER:
                addReminder(group,child);
                return true;

            default:
                return super.onContextItemSelected(menuItem);
        }
    }

    private void setNewDate(final int group, final int child) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                long totalMilliseconds = calendar.getTimeInMillis();
                listAdapter.changeDrugDate(group, child, totalMilliseconds);
            }
        }, year, month, day).show();
    }

    public void OnAddCategoryClick(View view){
        showAddCategoryDialog();
    }

    private EditText editText;
    private void showAddCategoryDialog(){
        editText = new EditText(this);
        new AlertDialog.Builder(this)
                .setView(editText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String categoryName = editText.getText().toString();
                        if (categoryName.length() == 0)
                            return;
                        if (!listAdapter.addCategory(categoryName))
                            showCategoryExistDialog();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void showCategoryExistDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Category creation")
                .setMessage("Category already exists.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void showRenameCategoryDialog(final int categoryPosition){
        final String oldName = listAdapter.getGroup(categoryPosition).getName();
        editText = new EditText(this);
        editText.setSelectAllOnFocus(true);
        editText.setText(oldName);
        new AlertDialog.Builder(this)
                .setView(editText)
                .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String categoryName = editText.getText().toString();
                        if (categoryName.length() == 0)
                            return;
                        listAdapter.renameCategory(categoryPosition, categoryName);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void showRenameDrugDialog(final int categoryPosition, final int drugPosition){
        final String oldName = listAdapter.getGroup(categoryPosition).getDrugs().get(drugPosition).getName();
        editText = new EditText(this);
        editText.setSelectAllOnFocus(true);
        editText.setText(oldName);
        new AlertDialog.Builder(this)
                .setView(editText)
                .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String drugName = editText.getText().toString();
                        if (drugName.length() == 0)
                            return;
                        listAdapter.renameDrug(categoryPosition, drugPosition, drugName);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void addDrug(final int categoryPosition) {
        editText = new EditText(this);
        new AlertDialog.Builder(this)
                .setView(editText)
                .setPositiveButton("Set expiration date", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String drugName = editText.getText().toString();
                        if (drugName.length() == 0)
                            return;

                        final Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        new DatePickerDialog(mainActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                long totalMilliseconds = calendar.getTimeInMillis();
                                listAdapter.addDrug(categoryPosition, drugName, totalMilliseconds);
                            }
                        }, year, month, day).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void addReminder(int categoryPosition, int drugPosition){
        Drug drug = listAdapter.getChild(categoryPosition,drugPosition);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, drug.getExpirationDate())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, drug.getExpirationDate())
                .putExtra(CalendarContract.Events.ALL_DAY,true)
                .putExtra(CalendarContract.Events.TITLE, "DRUG EXPIRATION")
                .putExtra(CalendarContract.Events.DESCRIPTION, listAdapter.getGroup(categoryPosition).getName() + " :: " + drug.getName() + " :: Expires today.")
                .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
        startActivity(intent);
    }
}