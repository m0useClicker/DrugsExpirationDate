package m0useclicker.drugsexpirationdate;

import java.util.Date;

public class Drug {

    private String name;

    private Date expirationDate;

    public Drug(String name, Date expirationDate) {
        this.name = name;
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}