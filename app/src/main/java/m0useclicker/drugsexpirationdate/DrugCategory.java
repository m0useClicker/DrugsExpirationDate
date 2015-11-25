package m0useclicker.drugsexpirationdate;

import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

class DrugCategory {

    private String name;

    private List<Drug> children;

    public DrugCategory(String name, Multimap<String, Date> drugs) {
        this.name = name;
        children = new ArrayList<>();
        for (Map.Entry<String, Date> drug : drugs.entries()) {
            children.add(new Drug(drug.getKey(), drug.getValue()));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public List<Drug> getDrugs() {
        return children;
    }
}