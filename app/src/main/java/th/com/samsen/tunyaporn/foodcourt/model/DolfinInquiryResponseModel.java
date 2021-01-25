package th.com.samsen.tunyaporn.foodcourt.model;

public class DolfinInquiryResponseModel {
    Boolean resultFlag;
    String resultCode;
    String resultInfo;
    String merchantNo;
    String merchantOrderNo;
    String merchantUserId;
    String finCustomerId;
    String transactionType;
    String transactionNo;
    Float orderAmount;
    String orderCurrency;
    String orderCountry;
    String orderType;
    String productType;

    public DolfinInquiryResponseModel() {
    }

    public DolfinInquiryResponseModel(Boolean resultFlag, String resultCode, String resultInfo, String merchantNo, String merchantOrderNo, String merchantUserId, String finCustomerId, String transactionType, String transactionNo, Float orderAmount, String orderCurrency, String orderCountry, String orderType, String productType) {
        this.resultFlag = resultFlag;
        this.resultCode = resultCode;
        this.resultInfo = resultInfo;
        this.merchantNo = merchantNo;
        this.merchantOrderNo = merchantOrderNo;
        this.merchantUserId = merchantUserId;
        this.finCustomerId = finCustomerId;
        this.transactionType = transactionType;
        this.transactionNo = transactionNo;
        this.orderAmount = orderAmount;
        this.orderCurrency = orderCurrency;
        this.orderCountry = orderCountry;
        this.orderType = orderType;
        this.productType = productType;
    }

    public class Column{
        public static final String resultFlag = "resultFlag";
        public static final String resultCode = "resultCode";
        public static final String resultInfo = "resultInfo";
        public static final String merchantNo = "merchantNo";
        public static final String merchantOrderNo = "merchantOrderNo";
        public static final String merchantUserId = "merchantUserId";
        public static final String finCustomerId = "finCustomerId";
        public static final String transactionType = "transactionType";
        public static final String transactionNo = "transactionNo";
        public static final String orderAmount = "orderAmount";
        public static final String orderCurrency = "orderCurrency";
        public static final String orderCountry = "orderCountry";
        public static final String orderType = "orderType";
        public static final String productType = "productType";
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(String merchantUserId) {
        this.merchantUserId = merchantUserId;
    }

    public String getFinCustomerId() {
        return finCustomerId;
    }

    public void setFinCustomerId(String finCustomerId) {
        this.finCustomerId = finCustomerId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getOrderCurrency() {
        return orderCurrency;
    }

    public void setOrderCurrency(String orderCurrency) {
        this.orderCurrency = orderCurrency;
    }

    public String getOrderCountry() {
        return orderCountry;
    }

    public void setOrderCountry(String orderCountry) {
        this.orderCountry = orderCountry;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
