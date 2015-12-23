package m0useclicker.drugsexpirationdate;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DrugCategoryUnitTests {
    private static final String categoryName = "categoryName";
    private static final String drug1Name = "drug1Name";
    private static final String drug2Name = "drug2Name";
    private static final Date date = new Date(1);
    private static final Drug drug1 = new Drug(drug1Name, date);
    private static final Drug drug2 = new Drug(drug2Name, date);
    private static DrugCategory category;

    @Before
    public void setUp() {
        ArrayList<Drug> drugs = new ArrayList<Drug>() {{
            add(drug1);
            add(drug2);
        }};
        category = new DrugCategory(categoryName, drugs);
    }

    @Test
    public void getName() throws Exception {
        assertEquals(categoryName, category.getName());
    }

    @Test
    public void setName() throws Exception {
        String newName = categoryName + "newName";
        category.setName(newName);
        assertEquals(newName, category.getName());
    }

    @Test
    public void getDrugs() throws Exception {
        assertEquals(drug1Name, category.getDrugs().get(0).getName());
    }
}