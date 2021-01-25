package th.com.samsen.tunyaporn.foodcourt.model;

public class Sell {

    public static final String DATABASE_NAME = "dbpointtickets.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE = "tm_sell";

    private String ID = "";
    private String PosID = "";
    private String MrtAmount = "";
    private String FoodName = "";
    private String BtnIdString = "";
    private String MRIP = "";
    private String Type = "";
    private String ShopIdString = "";
    private String BtnGP = "";
    private String BtnGP2 = "";
    private String Count = "";
    private String BtnFlag = "";
    private String BtnGPRule = "";
    private String BtnGPRule2 = "";


    public Sell() {
    }

    public class Column {
        public static final String ID = "ID";
        public static final String PosID = "PosID";
        public static final String MrtAmount = "MrtAmount";
        public static final String FoodName = "FoodName";
        public static final String MRIP = "MRIP";
        public static final String Type = "Type";
        public static final String BtnIdString = "BtnIdString";
        public static final String ShopIdString = "ShopIdString";
        public static final String BtnGP2 = "BtnGP2";
        public static final String BtnGP = "BtnGP";
        public static final String BtnGPRule = "BtnGPRule";
        public static final String BtnGPRule2 = "BtnGPRule2";
        public static final String BtnFlag = "BtnFlag";
    }

    public class ColumnIndex {
        public static final int BtnGPRule = 11;
        public static final int BtnGPRule2 = 12;
        public static final int PosID = 3;
        public static final int MrtAmount = 1;
        public static final int FoodName = 0;
        public static final int MRIP = 2;
        public static final int Type = 4;
        public static final int ID = 5;
        public static final int BtnIdString = 6;
        public static final int ShopIdString = 7;
        public static final int BtnGP2 = 9;
        public static final int BtnGP = 8;
        public static final int BtnFlag = 10;
    }

    public String getBtnGPRule() {
        return BtnGPRule;
    }

    public void setBtnGPRule(String btnGPRule) {
        BtnGPRule = btnGPRule;
    }

    public String getBtnGPRule2() {
        return BtnGPRule2;
    }

    public void setBtnGPRule2(String btnGPRule2) {
        BtnGPRule2 = btnGPRule2;
    }

    public String getBtnFlag() {
        return BtnFlag;
    }

    public void setBtnFlag(String btnFlag) {
        BtnFlag = btnFlag;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getBtnGP() {
        return BtnGP;
    }

    public void setBtnGP(String btnGP) {
        BtnGP = btnGP;
    }

    public String getBtnGP2() {
        return BtnGP2;
    }

    public void setBtnGP2(String btnGP2) {
        BtnGP2 = btnGP2;
    }

    public String getShopIdString() {
        return ShopIdString;
    }

    public void setShopIdString(String shopIdString) {
        ShopIdString = shopIdString;
    }

    public String getBtnIdString() {
        return BtnIdString;
    }

    public void setBtnIdString(String btnIdString) {
        BtnIdString = btnIdString;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMRIP() {
        return MRIP;
    }

    public void setMRIP(String MRIP) {
        this.MRIP = MRIP;
    }

    public String getPosID() {
        return PosID;
    }

    public void setPosID(String posID) {
        PosID = posID;
    }

    public String getMrtAmount() {
        return MrtAmount;
    }

    public void setMrtAmount(String mrtAmount) {
        MrtAmount = mrtAmount;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }
}
