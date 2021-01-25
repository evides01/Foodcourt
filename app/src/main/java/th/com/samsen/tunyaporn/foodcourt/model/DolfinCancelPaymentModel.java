package th.com.samsen.tunyaporn.foodcourt.model;

public class DolfinCancelPaymentModel {

    private boolean resultFlag;
    private String resultCode;
    private String resultInfo;
    private String merchantNo;
    private String merchantOrderNo = null;
    private String transactionNo = null;

    public DolfinCancelPaymentModel() {
    }

    public DolfinCancelPaymentModel(boolean resultFlag, String resultCode, String resultInfo, String merchantNo, String merchantOrderNo, String transactionNo) {
        this.resultFlag = resultFlag;
        this.resultCode = resultCode;
        this.resultInfo = resultInfo;
        this.merchantNo = merchantNo;
        this.merchantOrderNo = merchantOrderNo;
        this.transactionNo = transactionNo;
    }

    public class Column{
        public static final String resultFlag = "resultFlag";
        public static final String resultCode = "resultCode";
        public static final String resultInfo = "resultInfo";
        public static final String merchantNo = "merchantNo";
        public static final String merchantOrderNo = "merchantOrderNo";
        public static final String transactionNo = "transactionNo";
    }

    // Getter Methods

    public boolean getResultFlag() {
        return resultFlag;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    // Setter Methods

    public void setResultFlag(boolean resultFlag) {
        this.resultFlag = resultFlag;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

}
