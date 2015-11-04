package m0useclicker.drugsexpirationdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.google.common.collect.Multimap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class mainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        ExpandableListView list = (ExpandableListView) findViewById(R.id.listView);

        try {
            list.setAdapter(new DrugsListAdapter(this, getData()));
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
    }

    private List<DrugCategory> getData() throws ParseException {
        DrugsDbHelper dbHelper = new DrugsDbHelper(getBaseContext());
        List<DrugCategory> data = new ArrayList<>();

        for (String category : dbHelper.getCategories()) {
            Multimap<String, Date> drugData = dbHelper.getDrugs(category);
            DrugCategory drugCategory = new DrugCategory(category, drugData);
            data.add(drugCategory);
        }

        return data;
    }
}