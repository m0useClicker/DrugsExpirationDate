package m0useclicker.drugsexpirationdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
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

    private List<DrugCategory> getData() {
        List<DrugCategory> data = new ArrayList<>();

        for (String category : dbHelper.getCategories()) {
            ArrayList<Drug> drugData = dbHelper.getDrugs(category);
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

    public boolean addCategory(String categoryName) {
        Boolean isAdded = dbHelper.addCategory(categoryName);

        if (isAdded) {
            notifyDataChanged();
        }

        return isAdded;
    }

    public void removeCategory(int categoryPosition) {
        dbHelper.removeCategory(listData.get(categoryPosition).getName());
        notifyDataChanged();
    }

    public void removeDrug(int categoryPosition, int drugPosition) {
        Drug drug = getChild(categoryPosition, drugPosition);
        dbHelper.removeDrug(listData.get(categoryPosition).getName(), drug.getName());
        notifyDataChanged();
    }

    public void renameCategory(int categoryPosition, String newName) {
        dbHelper.renameCategory(listData.get(categoryPosition).getName(), newName);
        notifyDataChanged();
    }

    public void renameDrug(int categoryPosition, int drugPosition, String drugName) {
        dbHelper.renameDrug(listData.get(categoryPosition).getName(), listData.get(categoryPosition).getDrugs().get(drugPosition).getName(), drugName);
        notifyDataChanged();
    }

    public void addDrug(int categoryPosition, String drugName, long dateInMilliseconds) {
        dbHelper.addDrug(listData.get(categoryPosition).getName(), drugName, dateInMilliseconds);
        notifyDataChanged();
    }

    public void changeDrugDate(int categoryPosition, int drugPosition, long totalMilliseconds) {
        dbHelper.changeDrugDate(listData.get(categoryPosition).getName(), listData.get(categoryPosition).getDrugs().get(drugPosition).getName(), totalMilliseconds);
        notifyDataChanged();
    }

    public void refreshData() {
        listData = getData();
    }

    private void notifyDataChanged() {
        refreshData();
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
            expirationDate = (TextView) group.findViewById(R.id.expirationDate);
            expirationDate.setTextSize(12);
        }
    }
}

