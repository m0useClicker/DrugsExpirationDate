package m0useclicker.drugsexpirationdate;

import android.provider.BaseColumns;

class DrugsDbContract {
    public static abstract class DrugCategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME_ID = "category_id";
    }

    public static abstract class DrugEntry implements BaseColumns{
        public static final String TABLE_NAME = "drugs";
        public static final String COLUMN_NAME_CATEGORY = "drug_category";
        public static final String COLUMN_NAME_DRUG_NAME = "drug_name";
        public static final String COLUMN_NAME_EXPIRATION_DATE = "expiration_date";
    }
}