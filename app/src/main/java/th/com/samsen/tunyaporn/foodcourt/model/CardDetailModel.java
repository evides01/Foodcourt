package th.com.samsen.tunyaporn.foodcourt.model;

public class CardDetailModel {
    String CUSED;
    String CUSED1;
    String BARCODE;
    String CEXPIRED;
    String CEXPIRED_FLAG;
    String TYPEID;
    String CSTATUS;
    String CSTATUS_FLAG;
    String CBALANCE;

    public CardDetailModel() {
    }

    public CardDetailModel(String CUSED, String CUSED1, String BARCODE, String CEXPIRED, String CEXPIRED_FLAG, String TYPEID, String CSTATUS, String CSTATUS_FLAG, String CBALANCE) {
        this.CUSED = CUSED;
        this.CUSED1 = CUSED1;
        this.BARCODE = BARCODE;
        this.CEXPIRED = CEXPIRED;
        this.CEXPIRED_FLAG = CEXPIRED_FLAG;
        this.TYPEID = TYPEID;
        this.CSTATUS = CSTATUS;
        this.CSTATUS_FLAG = CSTATUS_FLAG;
        this.CBALANCE = CBALANCE;
    }

    public class Column{
        public static final String CUSED = "CUSED";
        public static final String CUSED1 = "CUSED1";
        public static final String BARCODE = "BARCODE";
        public static final String CEXPIRED = "CEXPIRED";
        public static final String CEXPIRED_FLAG = "CEXPIRED_FLAG";
        public static final String TYPEID = "TYPEID";
        public static final String CSTATUS = "CSTATUS";
        public static final String CSTATUS_FLAG = "CSTATUS_FLAG";
        public static final String CBALANCE = "CBALANCE";
    }

    public String getCUSED() {
        return CUSED;
    }

    public void setCUSED(String CUSED) {
        this.CUSED = CUSED;
    }

    public String getCUSED1() {
        return CUSED1;
    }

    public void setCUSED1(String CUSED1) {
        this.CUSED1 = CUSED1;
    }

    public String getBARCODE() {
        return BARCODE;
    }

    public void setBARCODE(String BARCODE) {
        this.BARCODE = BARCODE;
    }

    public String getCEXPIRED() {
        return CEXPIRED;
    }

    public void setCEXPIRED(String CEXPIRED) {
        this.CEXPIRED = CEXPIRED;
    }

    public String getCEXPIRED_FLAG() {
        return CEXPIRED_FLAG;
    }

    public void setCEXPIRED_FLAG(String CEXPIRED_FLAG) {
        this.CEXPIRED_FLAG = CEXPIRED_FLAG;
    }

    public String getTYPEID() {
        return TYPEID;
    }

    public void setTYPEID(String TYPEID) {
        this.TYPEID = TYPEID;
    }

    public String getCSTATUS() {
        return CSTATUS;
    }

    public void setCSTATUS(String CSTATUS) {
        this.CSTATUS = CSTATUS;
    }

    public String getCSTATUS_FLAG() {
        return CSTATUS_FLAG;
    }

    public void setCSTATUS_FLAG(String CSTATUS_FLAG) {
        this.CSTATUS_FLAG = CSTATUS_FLAG;
    }

    public String getCBALANCE() {
        return CBALANCE;
    }

    public void setCBALANCE(String CBALANCE) {
        this.CBALANCE = CBALANCE;
    }
}
