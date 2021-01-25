package th.com.samsen.tunyaporn.foodcourt.model;

public class AddCardModel {

    String BARCODE;
    String CUSED;
    String SLIPNO;
    String AMT;
    String ITEMBALANCE;
    String ITEMID;


    public static final String DATABASE_NAME = "dbpointtickets.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE = "tb_addcard";

    public class Column{
        public static final String BARCODE = "BARCODE";
        public static final String CUSED = "CUSED";
        public static final String SLIPNO = "SLIPNO";
        public static final String AMT = "AMT";
        public static final String ITEMBALANCE = "ITEMBALANCE";
        public static final String ITEMID = "ITEMID";
    }

    public AddCardModel() {
    }

    public String getITEMID() {
        return ITEMID;
    }

    public void setITEMID(String ITEMID) {
        this.ITEMID = ITEMID;
    }

    public String getBARCODE() {
        return BARCODE;
    }

    public void setBARCODE(String BARCODE) {
        this.BARCODE = BARCODE;
    }

    public String getCUSED() {
        return CUSED;
    }

    public void setCUSED(String CUSED) {
        this.CUSED = CUSED;
    }

    public String getSLIPNO() {
        return SLIPNO;
    }

    public void setSLIPNO(String SLIPNO) {
        this.SLIPNO = SLIPNO;
    }

    public String getAMT() {
        return AMT;
    }

    public void setAMT(String AMT) {
        this.AMT = AMT;
    }

    public String getITEMBALANCE() {
        return ITEMBALANCE;
    }

    public void setITEMBALANCE(String ITEMBALANCE) {
        this.ITEMBALANCE = ITEMBALANCE;
    }

    public AddCardModel(String BARCODE, String CUSED, String SLIPNO, String AMT, String ITEMBALANCE) {
        this.BARCODE = BARCODE;
        this.CUSED = CUSED;
        this.SLIPNO = SLIPNO;
        this.AMT = AMT;
        this.ITEMBALANCE = ITEMBALANCE;
    }
}
