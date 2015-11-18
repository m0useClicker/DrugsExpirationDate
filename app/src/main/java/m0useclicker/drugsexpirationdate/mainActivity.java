package m0useclicker.drugsexpirationdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.text.ParseException;

public class mainActivity extends Activity {

    private final int MENU_ADD_REMINDER = 0;
    private final int MENU_EDIT_DATE = 1;
    private final int MENU_RENAME = 2;
    private final int MENU_DELETE = 3;
    private final int MENU_ADD = 4;

    DrugsListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        ExpandableListView list = (ExpandableListView) findViewById(R.id.listView);
        registerForContextMenu(list);

        try {
            listAdapter = new DrugsListAdapter(this);

        } catch (ParseException e) {
            new AlertDialog.Builder(getBaseContext())
                    .setTitle("Error")
                    .setMessage("Unable to load data, database content has corrupted entries.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.stat_notify_error)
                    .show();
        }

        list.setAdapter(listAdapter);
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type =
                ExpandableListView.getPackedPositionType(info.packedPosition);

        int group =
                ExpandableListView.getPackedPositionGroup(info.packedPosition);

        int child =
                ExpandableListView.getPackedPositionChild(info.packedPosition);

        String positions = " group: " + group + " child: " + child;

        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            menu.setHeaderTitle("Drug" + positions);
            menu.add(0, MENU_ADD_REMINDER, 0, "Add calendar reminder");
            menu.add(0, MENU_EDIT_DATE, 0, "Change date");
            menu.add(0, MENU_RENAME, 0, "Rename");
            menu.add(0, MENU_DELETE, 1, "Delete");
        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            menu.setHeaderTitle("Category" + positions);
            menu.add(0, MENU_ADD, 0, "Add drug");
            menu.add(0, MENU_RENAME, 0, "Rename");
            menu.add(0, MENU_DELETE, 1, "Delete");
        }
    }

    public boolean onContextItemSelected(MenuItem menuItem) {
        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) menuItem.getMenuInfo();


        int group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int child = ExpandableListView.getPackedPositionChild(info.packedPosition);

        switch (menuItem.getItemId()) {
            case MENU_ADD:
                

            case MENU_DELETE:
                if(child!=-1){
                    listAdapter.removeGroup(group);
                }
            case MENU_ADD_REMINDER:
                new AlertDialog.Builder(getBaseContext())
                        .setTitle("Add reminder to calendar")
                        .setMessage("YOU GOT")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.stat_notify_error)
                        .show();
                return true;

            default:
                return super.onContextItemSelected(menuItem);
        }
    }
}