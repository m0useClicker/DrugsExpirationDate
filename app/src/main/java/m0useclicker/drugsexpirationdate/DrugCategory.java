package m0useclicker.drugsexpirationdate;

import java.util.ArrayList;
import java.util.List;

class DrugCategory {

    private String name;

    private List<Drug> children;

    public DrugCategory(String name, ArrayList<Drug> drugs) {
        this.name = name;
        children = drugs;
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