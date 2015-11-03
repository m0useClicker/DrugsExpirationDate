package m0useclicker.drugsexpirationdate;

import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DrugCategory {

    private String name;

    private List<Drug> children;

    public DrugCategory(String name, Multimap<String, Date> drugs) {
        this.name = name;
        children = new ArrayList<>();
        for (String drugName : drugs) {
            children.add(new Drug(drugName, new Date()));
        }
    }

    public String getName() {
        return name;
    }

    public List<Drug> getDrugs() {
        return children;
    }
}