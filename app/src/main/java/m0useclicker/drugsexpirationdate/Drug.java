package m0useclicker.drugsexpirationdate;

import java.util.Date;

class Drug {

    private String name;

    private Date expirationDate;

    public Drug(String name, Date expirationDate) {
        this.name = name;
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof Drug)
        {
            sameSame = name.equals(((Drug)object).getName()) && expirationDate.equals(((Drug)object).expirationDate);
        }

        return sameSame;
    }
}