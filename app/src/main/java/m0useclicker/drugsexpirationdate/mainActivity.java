package m0useclicker.drugsexpirationdate;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.common.collect.Multimap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class mainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        ExpandableListView list = (ExpandableListView) findViewById(R.id.listView);



        list.setAdapter(new DrugsListAdapter(this));
    }

    private List<DrugCategory> getData() throws ParseException {
        DrugsDbHelper dbHelper = new DrugsDbHelper(getBaseContext());

        for (String category:dbHelper.getCategories()){
            Multimap<String, Date> drugs = dbHelper.getDrugs(category);

            DrugCategory drugCategory = new DrugCategory(category,)
        }




    }
}