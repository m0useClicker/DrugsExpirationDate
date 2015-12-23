package m0useclicker.drugsexpirationdate;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Calendar;
import java.util.Date;

public class DrugUnitTests {
    private static final String name = "drugName";
    private static final Date date = new Date(1);
    private Drug drug;

    @Before
    public void setUp() {
        drug = new Drug(name, date);
    }

    @Test
    public void getName() throws Exception {
        assertEquals(name, drug.getName());
    }

    @Test
    public void setName() throws Exception {
        String newName = name + "newName";
        drug.setName(newName);
        assertEquals(newName, drug.getName());
    }

    @Test
    public void getExpirationDate() throws Exception {
        assertEquals(date, drug.getExpirationDate());
    }

    @Test
    public void setExpirationDate() throws Exception {
        Date newDate = new Date(2);
        drug.setExpirationDate(newDate);
        assertEquals(newDate, drug.getExpirationDate());
    }

    @Test
    public void drugsAreEqual() {
        String drugName = "drugName";
        Date expirationDate = Calendar.getInstance().getTime();

        Drug drugOne = new Drug(drugName, expirationDate);
        Drug drugTwo = new Drug(drugName, expirationDate);

        assertThat(drugOne, equalTo(drugTwo));
    }

    @Test
    public void drugsAreNotEqual() {
        String drugName = "drugName";
        Date expirationDate = Calendar.getInstance().getTime();

        Drug drugOne = new Drug(drugName, expirationDate);
        Drug drugTwo = new Drug(drugName + "other", expirationDate);

        assertThat(drugOne, not(equalTo(drugTwo)));
    }
}