package th.com.samsen.tunyaporn.foodcourt.model;

public class TenderModel {

    String TID;
    String CODE;
    String NAME;
    String VENDERCODE;
    String VENDERNAME;
    String CERRENCY;
    String PAYMENT_METHOD;
    String VALUE_CONVERT;
    String Server;
    String SEQ;
    String TERMINALID;
    String POSID;
    String MERCHAND_ID;
    String APP_ID;
    String CLIENT_KEY;
    String CLIENT_SECRET;
    String PREFIX_BARCODE;
    String REMARK1;
    String REMARK2;
    String REMARK3;
    String SCAN_TYPE;
    String VENDER_SHOP_ID;

    public TenderModel() {
    }

    public TenderModel(String TID, String CODE, String NAME, String VENDERCODE, String VENDERNAME, String CERRENCY, String PAYMENT_METHOD, String VALUE_CONVERT, String server, String SEQ, String TERMINALID, String POSID, String MERCHAND_ID, String APP_ID, String CLIENT_KEY, String CLIENT_SECRET, String PREFIX_BARCODE, String REMARK1, String REMARK2, String REMARK3, String SCAN_TYPE, String VENDER_SHOP_ID) {
        this.TID = TID;
        this.CODE = CODE;
        this.NAME = NAME;
        this.VENDERCODE = VENDERCODE;
        this.VENDERNAME = VENDERNAME;
        this.CERRENCY = CERRENCY;
        this.PAYMENT_METHOD = PAYMENT_METHOD;
        this.VALUE_CONVERT = VALUE_CONVERT;
        Server = server;
        this.SEQ = SEQ;
        this.TERMINALID = TERMINALID;
        this.POSID = POSID;
        this.MERCHAND_ID = MERCHAND_ID;
        this.APP_ID = APP_ID;
        this.CLIENT_KEY = CLIENT_KEY;
        this.CLIENT_SECRET = CLIENT_SECRET;
        this.PREFIX_BARCODE = PREFIX_BARCODE;
        this.REMARK1 = REMARK1;
        this.REMARK2 = REMARK2;
        this.REMARK3 = REMARK3;
        this.SCAN_TYPE = SCAN_TYPE;
        this.VENDER_SHOP_ID = VENDER_SHOP_ID;
    }

    public String getTID() {
        return TID;
    }

    public void setTID(String TID) {
        this.TID = TID;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getVENDERCODE() {
        return VENDERCODE;
    }

    public void setVENDERCODE(String VENDERCODE) {
        this.VENDERCODE = VENDERCODE;
    }

    public String getVENDERNAME() {
        return VENDERNAME;
    }

    public void setVENDERNAME(String VENDERNAME) {
        this.VENDERNAME = VENDERNAME;
    }

    public String getCERRENCY() {
        return CERRENCY;
    }

    public void setCERRENCY(String CERRENCY) {
        this.CERRENCY = CERRENCY;
    }

    public String getPAYMENT_METHOD() {
        return PAYMENT_METHOD;
    }

    public void setPAYMENT_METHOD(String PAYMENT_METHOD) {
        this.PAYMENT_METHOD = PAYMENT_METHOD;
    }

    public String getVALUE_CONVERT() {
        return VALUE_CONVERT;
    }

    public void setVALUE_CONVERT(String VALUE_CONVERT) {
        this.VALUE_CONVERT = VALUE_CONVERT;
    }

    public String getServer() {
        return Server;
    }

    public void setServer(String server) {
        Server = server;
    }

    public String getSEQ() {
        return SEQ;
    }

    public void setSEQ(String SEQ) {
        this.SEQ = SEQ;
    }

    public String getTERMINALID() {
        return TERMINALID;
    }

    public void setTERMINALID(String TERMINALID) {
        this.TERMINALID = TERMINALID;
    }

    public String getPOSID() {
        return POSID;
    }

    public void setPOSID(String POSID) {
        this.POSID = POSID;
    }

    public String getMERCHAND_ID() {
        return MERCHAND_ID;
    }

    public void setMERCHAND_ID(String MERCHAND_ID) {
        this.MERCHAND_ID = MERCHAND_ID;
    }

    public String getAPP_ID() {
        return APP_ID;
    }

    public void setAPP_ID(String APP_ID) {
        this.APP_ID = APP_ID;
    }

    public String getCLIENT_KEY() {
        return CLIENT_KEY;
    }

    public void setCLIENT_KEY(String CLIENT_KEY) {
        this.CLIENT_KEY = CLIENT_KEY;
    }

    public String getCLIENT_SECRET() {
        return CLIENT_SECRET;
    }

    public void setCLIENT_SECRET(String CLIENT_SECRET) {
        this.CLIENT_SECRET = CLIENT_SECRET;
    }

    public String getPREFIX_BARCODE() {
        return PREFIX_BARCODE;
    }

    public void setPREFIX_BARCODE(String PREFIX_BARCODE) {
        this.PREFIX_BARCODE = PREFIX_BARCODE;
    }

    public String getREMARK1() {
        return REMARK1;
    }

    public void setREMARK1(String REMARK1) {
        this.REMARK1 = REMARK1;
    }

    public String getREMARK2() {
        return REMARK2;
    }

    public void setREMARK2(String REMARK2) {
        this.REMARK2 = REMARK2;
    }

    public String getREMARK3() {
        return REMARK3;
    }

    public void setREMARK3(String REMARK3) {
        this.REMARK3 = REMARK3;
    }

    public String getSCAN_TYPE() {
        return SCAN_TYPE;
    }

    public void setSCAN_TYPE(String SCAN_TYPE) {
        this.SCAN_TYPE = SCAN_TYPE;
    }

    public String getVENDER_SHOP_ID() {
        return VENDER_SHOP_ID;
    }

    public void setVENDER_SHOP_ID(String VENDER_SHOP_ID) {
        this.VENDER_SHOP_ID = VENDER_SHOP_ID;
    }
}
