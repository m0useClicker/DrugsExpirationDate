package m0useclicker.drugsexpirationdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class DrugsListAdapter extends BaseExpandableListAdapter {

    private List<DrugCategory> listData;
    private Context context;
    private DrugsDbHelper dbHelper;

    public DrugsListAdapter(Context context) {
        this.context = context;
        dbHelper = new DrugsDbHelper(context);
        listData = getData();
    }

    private List<DrugCategory> getData(){
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
    public DrugCategory getGroup(int groupPosition) {
        return listData.get(groupPosition);
    }

    @Override
    public Drug getChild(int groupPosition, int childPosition) {
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
        DrugView drugView;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.drug, null);
            drugView = new DrugView(convertView);
            convertView.setTag(drugView);
        }
        drugView = (DrugView) convertView.getTag();

        drugView.drugName.setText(listData.get(groupPosition).getDrugs().get(childPosition).getName());
        drugView.expirationDate.setText(DateFormat.getDateInstance().format(listData.get(groupPosition).getDrugs().get(childPosition).getExpirationDate()));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean addCategory(String categoryName)
    {
        Multimap<String, Date> drugs = ArrayListMultimap.create();
        DrugCategory category = new DrugCategory(categoryName,drugs);

        listData.add(category);
        Boolean isAdded = dbHelper.addCategory(categoryName);

        if(isAdded)
            this.notifyDataSetChanged();

        return isAdded;
    }

    public void removeCategory(int categoryPosition) {
        dbHelper.removeCategory(listData.get(categoryPosition).getName());
        listData.remove(categoryPosition);
        this.notifyDataSetChanged();
    }

    public void removeDrug(int categoryPosition, int drugPosition) {
        Drug drug = getChild(categoryPosition, drugPosition);
        dbHelper.removeDrug(listData.get(categoryPosition).getName(), drug.getName());
        listData.get(categoryPosition).getDrugs().remove(drugPosition);
        this.notifyDataSetChanged();
    }

    public void renameCategory(int categoryPosition, String newName) {
        dbHelper.renameCategory(listData.get(categoryPosition).getName(), newName);
        listData.get(categoryPosition).setName(newName);
        this.notifyDataSetChanged();
    }

    public void renameDrug(int categoryPosition, int drugPosition, String drugName) {
        dbHelper.renameDrug(listData.get(categoryPosition).getName(), listData.get(categoryPosition).getDrugs().get(drugPosition).getName(), drugName);
        listData.get(categoryPosition).getDrugs().get(drugPosition).setName(drugName);
        this.notifyDataSetChanged();
    }

    public void addDrug(int categoryPosition, String drugName, long dateInMilliseconds) {
        dbHelper.addDrug(listData.get(categoryPosition).getName(), drugName, dateInMilliseconds);
        listData.get(categoryPosition).getDrugs().add(new Drug(drugName,new Date(dateInMilliseconds)));
        this.notifyDataSetChanged();
    }

    public void changeDrugDate(int categoryPosition, int drugPosition, long totalMilliseconds) {
        dbHelper.changeDrugDate(listData.get(categoryPosition).getName(), listData.get(categoryPosition).getDrugs().get(drugPosition).getName(), totalMilliseconds);
        listData.get(categoryPosition).getDrugs().get(drugPosition).setExpirationDate(new Date(totalMilliseconds));
        this.notifyDataSetChanged();
    }


    static class DrugCategoryView {
        final TextView groupHeader;

        private DrugCategoryView(View group) {
            groupHeader = (TextView) group.findViewById(R.id.drugCategoryView);
            groupHeader.setTextSize(16);
        }
    }

    static class DrugView {
        final TextView drugName;
        final TextView expirationDate;

        private DrugView(View group) {
            drugName = (TextView) group.findViewById(R.id.drugName);
            drugName.setTextSize(12);
            expirationDate = (TextView)group.findViewById(R.id.expirationDate);
            expirationDate.setTextSize(12);
        }
    }
}

