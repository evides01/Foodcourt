package th.com.samsen.tunyaporn.foodcourt.model;

public class Category {

    public static final String DATABASE_NAME = "dbpointtickets.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE = "tm_category";

    private String categoryName;
    private String MRIP;
    private String POSID;

    public Category() {
    }

    public Category(String categoryName, String MRIP, String POSID) {
        this.categoryName = categoryName;
        this.MRIP = MRIP;
        this.POSID = POSID;
    }

    public class Column {
        public static final String MRIP = "MRIP";
        public static final String POSID = "POSID";
        public static final String CategoryName = "CategoryName";
    }

    public String getPOSID() {
        return POSID;
    }

    public void setPOSID(String POSID) {
        this.POSID = POSID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMRIP() {
        return MRIP;
    }

    public void setMRIP(String MRIP) {
        this.MRIP = MRIP;
    }

}
