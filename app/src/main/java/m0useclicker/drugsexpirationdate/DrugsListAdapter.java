package m0useclicker.drugsexpirationdate;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class DrugsListAdapter extends BaseExpandableListAdapter {

    private List<DrugCategory> listData;
    private Context context;
    DrugsDbHelper dbHelper;

    public DrugsListAdapter(Context context) throws ParseException {
        this.context = context;
        dbHelper = new DrugsDbHelper(context);
        listData = getData();
    }

    private List<DrugCategory> getData() throws ParseException {
        List<DrugCategory> data = new ArrayList<>();

        for (String category : dbHelper.getCategories()) {
            Multimap<String, Date> drugData = dbHelper.getDrugs(category);
            DrugCategory drugCategory = new DrugCategory(category, drugData);
            data.add(drugCategory);
        }

        return data;
    }

    @Override
    public int getGroupCount() {
        return listData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listData.get(groupPosition).getDrugs().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listData.get(groupPosition).getDrugs().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return listData.get(groupPosition).getName().hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        DrugCategory group = listData.get(groupPosition);
        return (group.getName() + "+" + group.getDrugs().get(childPosition).getName()).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        DrugCategoryView drugCategoryView;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.drug_category, null);
            drugCategoryView = new DrugCategoryView(convertView);
            convertView.setTag(drugCategoryView);
        }
        drugCategoryView = (DrugCategoryView) convertView.getTag();

        drugCategoryView.groupHeader.setText(listData.get(groupPosition).getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        DrugView drugCategoryView;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.drug, null);
            drugCategoryView = new DrugView(convertView);
            convertView.setTag(drugCategoryView);
        }
        drugCategoryView = (DrugView) convertView.getTag();

        drugCategoryView.drugName.setText(listData.get(groupPosition).getDrugs().get(childPosition).getName());
        drugCategoryView.expirationDate.setText(listData.get(groupPosition).getDrugs().get(childPosition).getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void addGroup(String groupName)
    {
        Multimap<String, Date> drugs = ArrayListMultimap.create();
        DrugCategory category = new DrugCategory(groupName,drugs);

        listData.add(category);
        dbHelper.addCategory(groupName);

        this.notifyDataSetChanged();
    }

    public void removeGroup(int groupPosition) {
        DrugCategory category = (DrugCategory) getGroup(groupPosition);
        listData.remove(groupPosition);
        dbHelper.removeCategory(category.getName());
        this.notifyDataSetChanged();
    }

    static class DrugCategoryView {
        final TextView groupHeader;

        private DrugCategoryView(View group) {
            groupHeader = (TextView) group.findViewById(R.id.drugCategoryView);
        }
    }

    static class DrugView {
        final TextView drugName;
        final TextView expirationDate;

        private DrugView(View group) {
            drugName = (TextView) group.findViewById(R.id.drugName);
            expirationDate = (TextView)group.findViewById(R.id.expirationDate);
        }
    }
}

